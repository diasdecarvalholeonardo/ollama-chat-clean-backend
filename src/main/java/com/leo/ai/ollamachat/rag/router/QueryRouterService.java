package com.leo.ai.ollamachat.rag.router;

import com.leo.ai.ollamachat.llm.LLMClient;
import org.springframework.stereotype.Service;

@Service
public class QueryRouterService {

    private final LLMClient llm;

    public QueryRouterService(LLMClient llm) {
        this.llm = llm;
    }

    public QueryType route(String question) {

        String systemPrompt = """
        You are a strict query classifier.

        Classify the user question into EXACTLY ONE of these categories:

        RAG → if the answer depends on internal documents, database, or knowledge base
        LLM → if it is general knowledge or conversation
        WEB → if it requires recent, real-time, or external information

        Rules:
        - Return ONLY one word: RAG, LLM, or WEB
        - No punctuation
        - No explanation
        - If unsure, return RAG
        """;

        String response = llm.generate(question, systemPrompt);

        // 🔍 DEBUG (pode remover depois)
        System.out.println("🧠 Router raw response: " + response);

        return parseResponse(response);
    }

    private QueryType parseResponse(String response) {

        if (response == null) {
            return QueryType.RAG;
        }

        String normalized = response.trim().toUpperCase();

        if (normalized.contains("WEB")) return QueryType.WEB;
        if (normalized.contains("LLM")) return QueryType.LLM;

        return QueryType.RAG;
    }
}