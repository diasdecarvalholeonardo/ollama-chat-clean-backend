package com.leo.ai.ollamachat.agent.core;

import org.springframework.stereotype.Component;

import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;
import com.leo.ai.ollamachat.agent.context.service.ContextService;
import com.leo.ai.ollamachat.agent.core.llm.LLMAwareAgent;
import com.leo.ai.ollamachat.agent.core.prompt.PromptBuilder;
import com.leo.ai.ollamachat.agent.core.prompt.PromptContext;
import com.leo.ai.ollamachat.agent.infra.observability.AgentLogger;

@Component
public class ContextAwareAgent {

    private final ContextService contextService;
    private final PromptBuilder promptBuilder;
    private final LLMAwareAgent llmAgent;
    private final AgentLogger agentLogger;

    public ContextAwareAgent(
            ContextService contextService,
            PromptBuilder promptBuilder,
            LLMAwareAgent llmAgent,
            AgentLogger agentLogger
    ) {
        this.contextService = contextService;
        this.promptBuilder = promptBuilder;
        this.llmAgent = llmAgent;
        this.agentLogger = agentLogger;
    }

    public String handle(String userInput) {

    	ContextRequest request =
    		    new ContextRequest(userInput, "default-agent");


        ContextResponse context =
                contextService.resolveContext(request);

        PromptContext promptContext =
                new PromptContext(userInput, context);

        String prompt =
                promptBuilder.build(promptContext);

        // 🔹 Observabilidade — prompt
        agentLogger.logPrompt(prompt);

        String response =
                llmAgent.execute(prompt);

        // 🔹 Observabilidade — resposta
        agentLogger.logResponse(response);

        // 🔹 Observabilidade — latência total
        agentLogger.logLatency();

        return response;
    }

}

