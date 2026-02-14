package com.leo.ai.ollamachat.persistence.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.leo.ai.ollamachat.rag.model.RagRetrievedChunk;
import java.time.Instant;
import java.util.List;

@Document("rag_debug_traces")
public record RagDebugTraceDocument(

        @Id
        String id,

        Instant timestamp,
        String question,
        int topK,

        String embeddingModel,
        String llmModel,

        List<RagRetrievedChunk> retrievedChunks
) {}
