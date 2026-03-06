package com.leo.ai.ollamachat.rag.debug;

import java.time.Instant;
import java.util.List;

import com.leo.ai.ollamachat.persistence.mongo.debug.RagDebugTraceDocument;
import com.leo.ai.ollamachat.rag.model.RagRetrievedChunk;

public record RagDebugTraceView(
        String id,
        Instant timestamp,
        String question,
        int topK,
        String embeddingModel,
        String llmModel,
        List<RagRetrievedChunk> chunks
) {
    public static RagDebugTraceView from(RagDebugTraceDocument doc) {
        return new RagDebugTraceView(
                doc.id(),
                doc.timestamp(),
                doc.question(),
                doc.topK(),
                doc.embeddingModel(),
                doc.llmModel(),
                doc.retrievedChunks()
        );
    }
}


