package com.leo.ai.ollamachat.embedding.cache;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class EmbeddingCacheService {

    private static final int TTL_HOURS = 24;

    private final EmbeddingCacheRepository repository;

    public EmbeddingCacheService(EmbeddingCacheRepository repository) {
        this.repository = repository;
    }

    // =========================
    // 🔍 GET (COM TTL + REFRESH)
    // =========================
    public Optional<float[]> get(String text) {

        String normalized = normalize(text);
        String hash = hash(normalized);

        Optional<EmbeddingCacheEntry> entryOpt =
                repository.findByHash(hash);

        if (entryOpt.isEmpty()) {
            return Optional.empty();
        }

        EmbeddingCacheEntry entry = entryOpt.get();

        // 🔥 REMOVE SE EXPIRADO
        if (isExpired(entry)) {
            repository.delete(entry);
            return Optional.empty();
        }

        // 🔥 TOUCH (renova TTL automaticamente)
        entry.setExpiresAt(LocalDateTime.now().plusHours(TTL_HOURS));
        repository.save(entry);

        return Optional.of(entry.getEmbedding());
    }

    // =========================
    // 💾 SAVE (COM UPSERT)
    // =========================
    public void save(String text, float[] embedding) {

        if (embedding == null || embedding.length == 0) {
            return; // 🔥 proteção
        }

        String normalized = normalize(text);
        String hash = hash(normalized);

        Optional<EmbeddingCacheEntry> existing =
                repository.findByHash(hash);

        if (existing.isPresent()) {

            // 🔥 UPSERT (atualiza embedding + TTL)
            EmbeddingCacheEntry entry = existing.get();

            entry.setEmbedding(embedding);
            entry.setExpiresAt(LocalDateTime.now().plusHours(TTL_HOURS));

            repository.save(entry);
            return;
        }

        // 🔥 NOVO REGISTRO
        EmbeddingCacheEntry entry =
                new EmbeddingCacheEntry(hash, normalized, embedding);

        entry.setExpiresAt(LocalDateTime.now().plusHours(TTL_HOURS));

        repository.save(entry);
    }

    // =========================
    // 🔥 GET OU COMPUTA (NOVO)
    // =========================
    public float[] getOrCompute(String text, EmbeddingProvider provider) {

        return get(text).orElseGet(() -> {

            float[] embedding = provider.embed(text);

            save(text, embedding);

            return embedding;
        });
    }

    // =========================
    // 🧹 CLEANUP
    // =========================
    @Transactional
    public void cleanExpired() {
        repository.deleteExpired(LocalDateTime.now());
    }

    // =========================
    // 🔧 INTERNOS
    // =========================

    private boolean isExpired(EmbeddingCacheEntry entry) {
        return entry.getExpiresAt() != null &&
               entry.getExpiresAt().isBefore(LocalDateTime.now());
    }

    private String normalize(String text) {
        return text == null ? "" : text.trim().toLowerCase();
    }

    private String hash(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    // =========================
    // 🔌 INTERFACE FUNCIONAL
    // =========================
    public interface EmbeddingProvider {
        float[] embed(String text);
    }
}