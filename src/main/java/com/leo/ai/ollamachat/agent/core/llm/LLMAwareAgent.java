package com.leo.ai.ollamachat.agent.core.llm;

import org.springframework.stereotype.Component;

@Component
public class LLMAwareAgent {

    private final LLMClient llmClient;

    public LLMAwareAgent(LLMClient llmClient) {
        this.llmClient = llmClient;
    }

    public String execute(String prompt) {
        return llmClient.execute(prompt);
    }
}

