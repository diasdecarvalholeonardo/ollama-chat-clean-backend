package com.leo.ai.ollamachat.rag.debug;

import java.time.Instant;
import java.util.List;

import com.leo.ai.ollamachat.rag.model.RagRetrievedChunk;

public record RagDebugTrace(
        Instant timestamp,
        String question,
        int topK,
        List<RagRetrievedChunk> chunks
) {}
