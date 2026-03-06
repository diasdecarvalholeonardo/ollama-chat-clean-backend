package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.chat.dto.ChatRequest;
import com.leo.ai.ollamachat.chat.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagChatService {

    private final VectorSearchService vectorSearchService;
    private final ChatClient chatClient;

    public RagChatService(
            VectorSearchService vectorSearchService,
            ChatClient.Builder chatClientBuilder
    ) {
        this.vectorSearchService = vectorSearchService;
        this.chatClient = chatClientBuilder.build();
    }

    public ChatResponse chat(ChatRequest request) {

        List<Document> documents =
                vectorSearchService.search(request.getMessage(), 5);

        String context =
                documents.stream()
                        .map(Document::getContent)
                        .collect(Collectors.joining("\n\n"));

        String prompt =
                """
                Use the context below to answer the question.

                Context:
                %s

                Question:
                %s
                """.formatted(context, request.getMessage());

        String answer =
                chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();

        List<String> sources =
                documents.stream()
                        .map(d -> d.getMetadata().toString())
                        .collect(Collectors.toList());

        return new ChatResponse(answer, sources);
    }
}