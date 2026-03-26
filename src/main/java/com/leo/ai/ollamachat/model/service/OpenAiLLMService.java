package com.leo.ai.ollamachat.model.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAiLLMService implements LLMService {

    private final ChatClient chatClient;

    public OpenAiLLMService(ChatClient.Builder builder) {
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
