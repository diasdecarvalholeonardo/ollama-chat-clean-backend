package com.leo.ai.ollamachat.rag.model;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;

public record RagRetrievedChunk(
        String chunkId,
        String content
) {
    public static RagRetrievedChunk from(AgentKnowledgeBase kb) {
        return new RagRetrievedChunk(
                kb.getId().toString(),
                kb.getContent()
        );
    }
}
