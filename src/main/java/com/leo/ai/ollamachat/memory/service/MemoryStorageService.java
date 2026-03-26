package com.leo.ai.ollamachat.memory.service;

import com.leo.ai.ollamachat.embedding.service.EmbeddingService;
import com.leo.ai.ollamachat.memory.vector.model.VectorMemory;
import com.leo.ai.ollamachat.memory.vector.repository.VectorMemoryRepository;
import org.springframework.stereotype.Service;

@Service
public class MemoryStorageService {

    private final EmbeddingService embeddingService;
    private final VectorMemoryRepository repository;

    public MemoryStorageService(
            EmbeddingService embeddingService,
            VectorMemoryRepository repository) {

        this.embeddingService = embeddingService;
        this.repository = repository;
    }

    public void storeMemory(String sessionId, String memory) {

        float[] embedding = embeddingService.embed(memory);

        VectorMemory vectorMemory = new VectorMemory();
        vectorMemory.setSessionId(sessionId);
        vectorMemory.setContent(memory);
        vectorMemory.setEmbedding(embedding);

        repository.save(vectorMemory);
    }
}
