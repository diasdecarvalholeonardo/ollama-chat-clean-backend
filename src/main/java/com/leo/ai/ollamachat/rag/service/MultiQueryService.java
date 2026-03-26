package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MultiQueryService {

    private final ChatClient chatClient;

    public MultiQueryService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public List<String> generateQueries(String question) {

        String prompt = """
        Generate 4 alternative search queries for the following question.

        The goal is to retrieve relevant documents from a knowledge base.

        Question:
        %s

        Return each query on a new line.
        """.formatted(question);

        String response =
                chatClient
                        .prompt()
                        .user(prompt)
                        .call()
                        .content();

        return Arrays.stream(response.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
