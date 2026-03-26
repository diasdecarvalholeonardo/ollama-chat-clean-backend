package com.leo.ai.ollamachat.embedding.cache;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import com.leo.ai.ollamachat.domain.cache.SemanticCacheEntry;
import com.leo.ai.ollamachat.repository.jpa.cache.SemanticCacheRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class SemanticCacheService {

    private static final int TOP_K = 5;
    private static final double SIMILARITY_THRESHOLD = 0.90;

    private final SemanticCacheRepository repository;
    private final EmbeddingModel embeddingModel;

    public SemanticCacheService(
            SemanticCacheRepository repository,
            EmbeddingModel embeddingModel
    ) {
        this.repository = repository;
        this.embeddingModel = embeddingModel;
    }

    // =====================================================
    // 🟢 LEGADO — EMBEDDING DIRETO (COMPATÍVEL)
    // =====================================================
    public Optional<float[]> findSimilar(float[] queryEmbedding) {

        String embeddingStr = toPgVector(queryEmbedding);

        List<SemanticCacheEntry> candidates =
                repository.findTopKNearest(embeddingStr, TOP_K);

        return candidates.stream()
                .map(entry -> new ScoredEntry(
                        entry,
                        cosineSimilarity(queryEmbedding, entry.getEmbedding())
                ))
                .filter(se -> se.score >= SIMILARITY_THRESHOLD)
                .max(Comparator.comparingDouble(se -> se.score))
                .map(se -> se.entry.getEmbedding());
    }

    // =====================================================
    // 🟢 LEGADO — RESPOSTA POR EMBEDDING
    // =====================================================
    public Optional<String> findSimilarResponse(float[] queryEmbedding) {

        String embeddingStr = toPgVector(queryEmbedding);

        List<SemanticCacheEntry> candidates =
                repository.findTopKNearest(embeddingStr, TOP_K);

        return candidates.stream()
                .map(entry -> new ScoredEntry(
                        entry,
                        cosineSimilarity(queryEmbedding, entry.getEmbedding())
                ))
                .filter(se -> se.score >= SIMILARITY_THRESHOLD)
                .max(Comparator.comparingDouble(se -> se.score))
                .map(se -> se.entry.getResponse());
    }

    // =====================================================
    // 🔥 LEGADO — BUSCA POR TEXTO (COMPATÍVEL)
    // =====================================================
    public Optional<String> findSimilar(String query) {

        if (query == null || query.isBlank()) {
            return Optional.empty();
        }

        float[] embedding = embeddingModel.embed(query);

        if (embedding == null || embedding.length == 0) {
            return Optional.empty();
        }

        return findSimilarResponse(embedding);
    }

    // =====================================================
    // 🚀 NOVO — SCORE BASED CACHE (PARA FUSION)
    // =====================================================
    public Optional<CachedResult> findBestMatch(String query) {

        if (query == null || query.isBlank()) {
            return Optional.empty();
        }

        float[] embedding = embeddingModel.embed(query);

        if (embedding == null || embedding.length == 0) {
            return Optional.empty();
        }

        String embeddingStr = toPgVector(embedding);

        List<SemanticCacheEntry> candidates =
                repository.findTopKNearest(embeddingStr, TOP_K);

        return candidates.stream()
                .map(entry -> {
                    double score = cosineSimilarity(embedding, entry.getEmbedding());
                    return new CachedResult(entry.getResponse(), score);
                })
                .max(Comparator.comparingDouble(CachedResult::getScore));
    }

    // =====================================================
    // 💾 SAVE (COMPATÍVEL)
    // =====================================================
    public void save(String query, String response, float[] embedding) {

        if (query == null || query.isBlank() || response == null) return;

        SemanticCacheEntry entry =
                new SemanticCacheEntry(query, response, embedding);

        repository.save(entry);
    }

    // =====================================================
    // 🔥 SAVE AUTOMÁTICO (NOVO)
    // =====================================================
    public void store(String query, String response) {

        if (query == null || query.isBlank() || response == null) return;

        float[] embedding = embeddingModel.embed(query);

        if (embedding == null || embedding.length == 0) return;

        save(query, response, embedding);
    }

    // =====================================================
    // 🔥 COSINE SIMILARITY (SEGURA)
    // =====================================================
    private double cosineSimilarity(float[] a, float[] b) {

        if (a == null || b == null || a.length != b.length) return 0;

        double dot = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }

        if (normA == 0 || normB == 0) return 0;

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    // =====================================================
    // 📦 AUXILIAR
    // =====================================================
    private static class ScoredEntry {
        SemanticCacheEntry entry;
        double score;

        ScoredEntry(SemanticCacheEntry entry, double score) {
            this.entry = entry;
            this.score = score;
        }
    }

    // =====================================================
    // 🔥 CONVERSÃO float[] → pgvector STRING
    // =====================================================
    private String toPgVector(float[] embedding) {

        if (embedding == null || embedding.length == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");

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