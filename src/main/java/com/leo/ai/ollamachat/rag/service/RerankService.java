package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RerankService {

    private static final int MAX_DOCS = 5;
    private static final int MAX_CONTENT_CHARS = 500;

    private final ChatModel chatModel;

    public RerankService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public List<DocumentChunk> rerank(String query, List<DocumentChunk> documents) {

        if (documents == null || documents.isEmpty()) {
            return documents;
        }

        // 🔥 1. ENUMERA DOCUMENTOS
        String docsText = buildDocumentsText(documents);

        // 🔥 2. PROMPT
        String promptText = """
                You are a ranking system.

                TASK:
                Select the most relevant documents for answering the query.

                RULES:
                - Return ONLY a comma-separated list of document indices
                - Example: 2,0,3
                - No explanation
                - No text
                - If unsure, return best guess

                Query:
                %s

                Documents:
                %s

                Top documents:
                """.formatted(query, docsText);

        String response;

        try {
            response = chatModel.call(new Prompt(promptText))
                    .getResult()
                    .getOutput()
                    .getContent()
                    .trim();
        } catch (Exception e) {
            return fallback(documents);
        }

        // 🔥 3. PARSE
        List<Integer> rankedIndexes = parseIndexes(response);

        // 🔥 4. REORDENA
        List<DocumentChunk> reranked = rankedIndexes.stream()
                .filter(i -> i >= 0 && i < documents.size())
                .map(documents::get)
                .distinct()
                .limit(MAX_DOCS)
                .collect(Collectors.toList());

        // 🔥 5. FALLBACK
        if (reranked.isEmpty()) {
            return fallback(documents);
        }

        return reranked;
    }

    // 🔥 Builder separado (clean code)
    private String buildDocumentsText(List<DocumentChunk> documents) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < documents.size(); i++) {
            builder.append("DOC ")
                    .append(i)
                    .append(":\n")
                    .append(truncate(documents.get(i).getContent(), MAX_CONTENT_CHARS))
                    .append("\n\n");
        }

        return builder.toString();
    }

    // 🔥 Parser resiliente
    private List<Integer> parseIndexes(String response) {

        if (response == null || response.isBlank()) {
            return List.of();
        }

        try {
            return Arrays.stream(response.split(","))
                    .map(String::trim)
                    .map(s -> s.replaceAll("[^0-9]", "")) // remove lixo
                    .filter(s -> !s.isBlank())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return List.of();
        }
    }

    // 🔥 Fallback seguro
    private List<DocumentChunk> fallback(List<DocumentChunk> documents) {
        return documents.stream()
                .limit(MAX_DOCS)
                .collect(Collectors.toList());
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() > max ? text.substring(0, max) : text;
    }
}