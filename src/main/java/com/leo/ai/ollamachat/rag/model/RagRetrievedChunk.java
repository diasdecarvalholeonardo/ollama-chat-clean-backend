package com.leo.ai.ollamachat.rag.model;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;

public record RagRetrievedChunk(

        String chunkId,
        String content,

        // 🔹 novo campo
        Double similarityScore,

        // 🔹 metadata de origem
        String sourceDocument,
        Integer pageNumber

) {

    /**
     * 🔒 Método consagrado existente
     */
    public static RagRetrievedChunk from(AgentKnowledgeBase kb) {
        return new RagRetrievedChunk(
                kb.getId().toString(),
                kb.getContent(),
                null,
                kb.getSourceDocument(),
                kb.getPageNumber()
        );
    }

    /**
     * 🔹 Factory com similarity score
     */
    public static RagRetrievedChunk from(
            AgentKnowledgeBase kb,
            double similarityScore
    ) {
        return new RagRetrievedChunk(
                kb.getId().toString(),
                kb.getContent(),
                similarityScore,
                kb.getSourceDocument(),
                kb.getPageNumber()
        );
    }

    /**
     * 🔹 Construtor de compatibilidade
     */
    public RagRetrievedChunk(String chunkId, String content) {
        this(chunkId, content, null, null, null);
    }
}