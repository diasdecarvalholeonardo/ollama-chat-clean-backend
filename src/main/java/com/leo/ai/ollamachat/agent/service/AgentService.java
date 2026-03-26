package com.leo.ai.ollamachat.agent.service;

import com.leo.ai.ollamachat.agent.tool.AgentTool;
import com.leo.ai.ollamachat.memory.service.ChatMemoryService;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    private final ChatClient chatClient;
    private final AgentToolRegistry toolRegistry;
    private final ChatMemoryService chatMemoryService;

    public AgentService(ChatClient.Builder chatClientBuilder,
            AgentToolRegistry toolRegistry,
            ChatMemoryService chatMemoryService) {

              this.chatClient = chatClientBuilder.build();
              this.toolRegistry = toolRegistry;
              this.chatMemoryService = chatMemoryService;}

    public String runAgent(String question) {

        String toolSelectionPrompt = buildToolSelectionPrompt(question);

        String toolName =
                chatClient.prompt()
                        .user(toolSelectionPrompt)
                        .call()
                        .content()
                        .trim();

        AgentTool tool = toolRegistry.getTool(toolName);

        if (tool == null) {
            return fallbackAnswer(question);
        }

        String toolResult = tool.execute(question);

        return generateFinalAnswer(question, toolName, toolResult);
    }

    private String buildToolSelectionPrompt(String question) {

        return """
        You are an AI agent.

        Decide which tool should be used to answer the question.

        Available tools:

        searchDocuments -> search the internal knowledge base
        webSearch -> search the internet

        Respond ONLY with the tool name.

        Question:
        %s
        """.formatted(question);
    }

    private String generateFinalAnswer(String question,
                                       String toolName,
                                       String toolResult) {

        String finalPrompt = """
        You are an AI assistant.

        The following information was retrieved using the tool: %s

        Tool result:
        %s

        Using this information, answer the question:

        %s
        """.formatted(toolName, toolResult, question);

        return chatClient.prompt()
                .user(finalPrompt)
                .call()
                .content();
    }

    private String fallbackAnswer(String question) {

        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}
