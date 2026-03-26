package com.leo.ai.ollamachat.rag.debug;

import com.leo.ai.ollamachat.rag.model.RagRetrievedChunk;

import java.time.Instant;
import java.util.List;

public record RagDebugTrace(

        // 🔹 campos originais (mantidos)
        Instant timestamp,
        String question,
        int topK,
        List<RagRetrievedChunk> chunks,

        // 🔹 novos campos para debug avançado
        String contextUsed,
        String promptSentToLLM,
        String modelAnswer,
        long retrievalTimeMs,
        long generationTimeMs

) {

    // 🔒 Construtor compatível com versão antiga
    public RagDebugTrace(
            Instant timestamp,
            String question,
            int topK,
            List<RagRetrievedChunk> chunks
    ) {
        this(
                timestamp,
                question,
                topK,
                chunks,
                null,
                null,
                null,
                0,
                0
        );
    }
}