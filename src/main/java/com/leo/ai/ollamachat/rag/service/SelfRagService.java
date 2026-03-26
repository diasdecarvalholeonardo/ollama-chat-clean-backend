package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SelfRagService {

    private final ChatClient chatClient;

    public SelfRagService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public boolean verify(String question, String answer, String context) {

        String prompt = """
        You are verifying whether an AI answer is supported by the context.

        If the answer is fully supported by the context, return YES.
        If the answer contains information not present in the context, return NO.

        Question:
        %s

        Answer:
        %s

        Context:
        %s
        """.formatted(question, answer, context);

        String result =
                chatClient
                        .prompt()
                        .user(prompt)
                        .call()
                        .content();

        return result.toUpperCase().contains("YES");
    }
}
