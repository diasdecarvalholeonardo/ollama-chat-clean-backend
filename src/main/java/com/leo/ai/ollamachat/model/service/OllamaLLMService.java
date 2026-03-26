package com.leo.ai.ollamachat.model.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaLLMService implements LLMService {

    private final ChatClient chatClient;

    public OllamaLLMService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Override
    public String generate(String prompt) {

        return chatClient
                .prompt(prompt)
                .call()
                .content();

    }
}
