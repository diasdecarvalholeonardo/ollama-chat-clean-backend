package com.leo.ai.ollamachat.embedding.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.embedding.repository.EmbeddingRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentEmbeddingService {

    private final EmbeddingService embeddingService;
    private final EmbeddingRepository repository;

    public DocumentEmbeddingService(
            EmbeddingService embeddingService,
            EmbeddingRepository repository) {

        this.embeddingService = embeddingService;
        this.repository = repository;
    }

    public DocumentChunk generateAndStoreEmbedding(DocumentChunk chunk) {

        float[] embedding =
                embeddingService.generateEmbedding(chunk.getContent());

        chunk.setEmbedding(embedding);

        return repository.save(chunk);
    }
}
