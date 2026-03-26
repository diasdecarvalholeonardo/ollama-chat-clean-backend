package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.leo.ai.ollamachat.chat.dto.ChatRequest;
import com.leo.ai.ollamachat.domain.cache.SemanticCacheEntry;
import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.rag.repository.VectorRepository;
import com.leo.ai.ollamachat.repository.jpa.cache.SemanticCacheRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final VectorRepository repository;
    private final SemanticCacheRepository cacheRepository;

    public VectorSearchService(
            VectorStore vectorStore,
            EmbeddingModel embeddingModel,
            VectorRepository repository,
            SemanticCacheRepository cacheRepository
    ) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.repository = repository;
        this.cacheRepository = cacheRepository;
    }

    /**
     * 🔥 PIPELINE COMPLETO (CACHE → PGVECTOR → FALLBACK)
     */
    public List<DocumentChunk> search(String query, int topK) {

        if (query == null || query.isBlank()) {
            return List.of();
        }

        try {
            // 1️⃣ Gera embedding real
            float[] embeddingArray = embeddingModel.embed(query);

            if (embeddingArray == null || embeddingArray.length == 0) {
                return List.of();
            }

            String embedding = toPgVector(embeddingArray);

            // 2️⃣ 🔥 TENTA CACHE SEMÂNTICO PRIMEIRO
            List<SemanticCacheEntry> cacheResults =
                    cacheRepository.findTop1Similar(embedding);

            if (cacheResults != null && !cacheResults.isEmpty()) {

                SemanticCacheEntry cache = cacheResults.get(0);

                System.out.println("⚡ CACHE HIT!");

                DocumentChunk chunk = new DocumentChunk();
                chunk.setId(cache.getId());
                chunk.setContent(cache.getResponse());

                return List.of(chunk);
            }

            // 3️⃣ 🔥 BUSCA REAL (pgvector)
            List<DocumentChunk> results =
                    repository.searchSimilar(embedding, topK);

            if (results != null && !results.isEmpty()) {
                return results;
            }

            // 4️⃣ 🔁 FALLBACK (Spring AI VectorStore)
            return fallbackSearch(query, topK);

        } catch (Exception e) {
            System.err.println("Erro na busca híbrida:");
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * 🔁 Fallback usando VectorStore (Spring AI)
     */
    private List<DocumentChunk> fallbackSearch(String query, int topK) {

        try {
            SearchRequest request = SearchRequest.builder()
                    .query(query)
                    .topK(topK)
                    .build();

            List<Document> documents =
                    vectorStore.similaritySearch(request);

            return documents
                    .stream()
                    .map(this::convertToChunk)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Erro no fallback:");
            e.printStackTrace();
            return List.of();
        }
    }

    /**
     * 🔹 Entrada via ChatRequest
     */
    public List<DocumentChunk> search(ChatRequest request, int topK) {

        if (request == null || request.getMessage() == null) {
            return List.of();
        }

        return search(request.getMessage(), topK);
    }

    /**
     * 🔹 Conversão Document → DocumentChunk
     */
    private DocumentChunk convertToChunk(Document doc) {

        DocumentChunk chunk = new DocumentChunk();

        if (doc == null) {
            return chunk;
        }

        chunk.setContent(doc.getContent());

        if (doc.getMetadata() != null) {

            Object id = doc.getMetadata().get("chunk_id");

            if (id instanceof Number) {
                chunk.setId(((Number) id).longValue());
            }
        }

        return chunk;
    }

    /**
     * 🔥 float[] → formato pgvector
     */
    private String toPgVector(float[] embedding) {

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < embedding.length; i++) {
            sb.append(embedding[i]);

            if (i < embedding.length - 1) {
                sb.append(",");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}