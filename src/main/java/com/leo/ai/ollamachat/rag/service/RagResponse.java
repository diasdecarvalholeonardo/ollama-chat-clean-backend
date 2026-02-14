package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;
import com.leo.ai.ollamachat.rag.debug.RagDebugTrace;
import java.util.List;

public record RagResponse(
        String answer,
        List<AgentKnowledgeBase> sources,
        RagDebugTrace debugTrace
) {

    // 🔒 Construtor consagrado — compatibilidade total
    public RagResponse(String answer, List<AgentKnowledgeBase> sources) {
        this(answer, sources, null);
    }
}

