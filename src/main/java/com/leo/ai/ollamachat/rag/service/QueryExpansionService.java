package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryExpansionService {

    private static final int MAX_TERMS = 5; // fallback se LLM falhar
    private final ChatModel chatModel;

    public QueryExpansionService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String expand(String query, String memoryContext) {
        if (memoryContext == null || memoryContext.isBlank()) {
            return query;
        }

        try {
            // 🔥 Tenta expansão inteligente via LLM
            String trimmedMemory = truncate(memoryContext, 500);

            String prompt = """
                    You are a query optimizer for search systems.

                    TASK:
                    Improve the user query using relevant context.

                    RULES:
                    - Keep the original intent
                    - Add only relevant terms from the context
                    - DO NOT invent information
                    - DO NOT explain anything
                    - Return ONLY the improved query

                    Query:
                    %s

                    Context:
                    %s

                    Improved Query:
                    """.formatted(query, trimmedMemory);

            String response = chatModel.call(new Prompt(prompt))
                    .getResult()
                    .getOutput()
                    .getContent()
                    .trim();

            if (response.length() >= 5) {
                return sanitize(response);
            }
        } catch (Exception e) {
            // fallback para método simples
        }

        // 🔥 fallback: extração de palavras-chave da memória
        List<String> keywords = extractKeywords(memoryContext);
        keywords = keywords.stream().limit(MAX_TERMS).toList();
        return query + " " + String.join(" ", keywords);
    }

    // ================= HELPERS =================

    private String truncate(String text, int max) {
        return text.length() > max ? text.substring(0, max) : text;
    }

    private String sanitize(String text) {
        return text.replace("\n", " ")
                   .replaceAll("\\s+", " ")
                   .trim();
    }

    private List<String> extractKeywords(String text) {
        return Arrays.stream(text.toLowerCase()
                        .replaceAll("[^a-z0-9 ]", " ")
                        .split("\\s+"))
                .filter(this::isUsefulWord)
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean isUsefulWord(String word) {
        if (word.length() < 4) return false;
        Set<String> stopwords = Set.of(
                "this","that","with","have","from","para","como","isso",
                "the","and","for","are","was","were","com","uma","por"
        );
        return !stopwords.contains(word);
    }
}