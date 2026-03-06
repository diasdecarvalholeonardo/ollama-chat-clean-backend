package com.leo.ai.ollamachat.ingestion.service;

import com.leo.ai.ollamachat.service.document.DocumentChunkService; 
import com.leo.ai.ollamachat.vector.service.VectorStoreService;
import org.springframework.stereotype.Service;

@Service
public class IngestionService {

    private final DocumentChunkService chunkService;
    private final VectorStoreService vectorStoreService;

    public IngestionService(
            DocumentChunkService chunkService,
            VectorStoreService vectorStoreService) {

        this.chunkService = chunkService;
        this.vectorStoreService = vectorStoreService;
    }

    public void ingest(String text) {

        var chunks = chunkService.chunkAndSave(text);

        chunks.forEach(chunk ->
                vectorStoreService.save(chunk.getContent())
        );
    }
}
