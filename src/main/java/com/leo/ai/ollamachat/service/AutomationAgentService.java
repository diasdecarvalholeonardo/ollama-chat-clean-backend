package com.leo.ai.ollamachat.service;

import com.leo.ai.ollamachat.tool.AutomationAgentTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class AutomationAgentService {

    private final ChatClient chatClient;
    private final AutomationAgentTool tool;

    public AutomationAgentService(
            ChatModel chatModel,
            AutomationAgentTool tool
    ) {
        this.chatClient = ChatClient.builder(chatModel).build();
        this.tool = tool;
    }

    /**
     * Agente simples e estável (Spring AI M4)
     */
    public String runAgent(String userPrompt) {

        if (userPrompt == null || userPrompt.isBlank()) {
            return "⚠️ Prompt vazio.";
        }

        String prompt = userPrompt.toLowerCase();

        // 🔹 Decisão explícita (TOOLS)
        if (prompt.contains("health") || prompt.contains("status")) {
            return tool.checkSystemHealth();
        }

        if (prompt.startsWith("echo ")) {
            return tool.echo(userPrompt.substring(5));
        }

        // 🔹 Fallback → LLM (Ollama)
        return chatClient
                .prompt()
                .user(userPrompt)
                .call()
                .content();
    }
}
