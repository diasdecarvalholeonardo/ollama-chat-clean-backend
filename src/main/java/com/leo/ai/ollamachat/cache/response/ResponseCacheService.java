package com.leo.ai.ollamachat.cache.response;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ResponseCacheService {

    private static final int TOP_K = 5;
    private static final double THRESHOLD = 0.92;
    private static final int TTL_HOURS = 6;

    private final ResponseCacheRepository repository;

    public ResponseCacheService(ResponseCacheRepository repository) {
        this.repository = repository;
    }

    public Optional<String> findSimilar(float[] queryEmbedding) {

        List<ResponseCacheEntry> candidates =
                repository.findTopK(queryEmbedding, TOP_K);

        return candidates.stream()
                .map(e -> new Scored(e, cosine(queryEmbedding, e.getEmbedding())))
                .filter(s -> s.score >= THRESHOLD)
                .max(Comparator.comparingDouble(s -> s.score))
                .map(s -> s.entry.getResponse());
    }

    public void save(String query, String response, float[] embedding) {

        ResponseCacheEntry entry =
                new ResponseCacheEntry(query, response, embedding);

        entry.setExpiresAt(LocalDateTime.now().plusHours(TTL_HOURS));

        repository.save(entry);
    }

    @Transactional
    public int cleanExpired() {
        return repository.deleteExpired(LocalDateTime.now());
    }

    // =========================

    private double cosine(float[] a, float[] b) {
        double dot = 0, na = 0, nb = 0;

        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na += a[i] * a[i];
            nb += b[i] * b[i];
        }

        return dot / (Math.sqrt(na) * Math.sqrt(nb));
    }

    private static class Scored {
        ResponseCacheEntry entry;
        double score;

        Scored(ResponseCacheEntry e, double s) {
            this.entry = e;
            this.score = s;
        }
    }
}
