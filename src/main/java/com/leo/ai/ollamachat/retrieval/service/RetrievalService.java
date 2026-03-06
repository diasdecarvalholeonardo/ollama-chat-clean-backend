package com.leo.ai.ollamachat.retrieval.service;

import org.springframework.stereotype.Service;
import java.util.List;
import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.domain.document.DocumentChunkRepository;
import com.leo.ai.ollamachat.embedding.EmbeddingService;

@Service
public class RetrievalService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository repository;

    public RetrievalService(EmbeddingService embeddingService,
                            DocumentChunkRepository repository) {
        this.embeddingService = embeddingService;
        this.repository = repository;
    }

    public List<DocumentChunk> searchSimilar(String query, int limit) {

        float[] queryEmbedding = embeddingService.generateEmbedding(query);

        return repository.findTopSimilar(queryEmbedding, limit);
    }
}
