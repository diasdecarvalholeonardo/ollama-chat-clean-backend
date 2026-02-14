package com.leo.ai.ollamachat.rag.service;

import java.time.Instant;
import java.util.List;

public record RagDebugTrace(
        Instant timestamp,
        String question,
        int topK,
        int retrievedChunks
) {}

