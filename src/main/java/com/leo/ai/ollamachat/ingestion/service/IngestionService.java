package com.leo.ai.ollamachat.ingestion.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.rag.repository.VectorRepository;
import com.leo.ai.ollamachat.service.document.DocumentChunkService;
import com.leo.ai.ollamachat.vector.service.VectorStoreService;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IngestionService {

    // 🟢 PIPELINE ORIGINAL (mantido)
    private final DocumentChunkService chunkService;
    private final VectorStoreService vectorStoreService;

    // 🔵 NOVO (RAG direto com pgvector)
    private final EmbeddingModel embeddingModel;
    private final VectorRepository repository;

    public IngestionService(
            DocumentChunkService chunkService,
            VectorStoreService vectorStoreService,
            EmbeddingModel embeddingModel,
            VectorRepository repository
    ) {
        this.chunkService = chunkService;
        this.vectorStoreService = vectorStoreService;
        this.embeddingModel = embeddingModel;
        this.repository = repository;
    }

    /**
     * 🟢 MÉTODO ORIGINAL (mantido)
     */
    public void ingest(String text) {

        var chunks = chunkService.chunkAndSave(text);

        chunks.forEach(chunk ->
                vectorStoreService.save(chunk.getContent())
        );
    }

    /**
     * 🔵 INGESTÃO MELHORADA (pgvector direto)
     */
    public void ingestText(String text) {

        List<String> chunks = chunk(text);

        String documentId = UUID.randomUUID().toString();
        int index = 0;

        for (String chunk : chunks) {

            float[] embedding = embeddingModel.embed(chunk);

            DocumentChunk doc = new DocumentChunk();
            doc.setContent(chunk);

            // 🔥 IMPORTANTE: conversão para pgvector
            doc.setEmbedding(embedding);

            doc.setChunkIndex(index++);
            doc.setSource("TEXT");
            doc.setDocumentId(documentId);

            repository.save(doc);
        }
    }

    /**
     * 🔥 Chunking MELHORADO (com overlap)
     */
    private List<String> chunk(String text) {

        int chunkSize = 500;
        int overlap = 100;

        List<String> chunks = new ArrayList<>();

        for (int i = 0; i < text.length(); i += (chunkSize - overlap)) {

            int end = Math.min(i + chunkSize, text.length());
            String piece = text.substring(i, end).trim();

            if (!piece.isEmpty()) {
                chunks.add(piece);
            }
        }

        return chunks;
    }

}