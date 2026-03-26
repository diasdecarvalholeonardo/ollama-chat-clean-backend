package com.leo.ai.ollamachat.embedding.cache;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "embedding_cache")
public class EmbeddingCacheEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 HASH (já usado no seu service)
    @Column(unique = true)
    private String hash;

    // 🔥 TEXTO NORMALIZADO
    @Column(columnDefinition = "TEXT")
    private String text;

    // 🔥 NOVO — QUERY/RESPONDE para SemanticCacheService
    @Column(columnDefinition = "TEXT")
    private String query;

    @Column(columnDefinition = "TEXT")
    private String response;

    // 🔥 EMBEDDING
    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    // 🔥 NOVO — TTL
    private LocalDateTime expiresAt;

    // =========================
    // ✅ CONSTRUTOR PADRÃO
    // =========================
    public EmbeddingCacheEntry() {} // default

 // construtor para hash/text/embedding
 public EmbeddingCacheEntry(String hash, String text, float[] embedding) {
     this.hash = hash;
     this.text = text;
     this.embedding = embedding;
 }

 // construtor para query/response/embedding
 public EmbeddingCacheEntry(String query, String response, float[] embedding, boolean semantic) {
     this.query = query;
     this.response = response;
     this.embedding = embedding;
 }

    // =========================
    // ✅ GETTERS
    // =========================
    public Long getId() { return id; }
    public String getHash() { return hash; }
    public String getText() { return text; }
    public float[] getEmbedding() { return embedding; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public String getResponse() { return response; }
    public String getQuery() { return query; }

    // =========================
    // 🔥 SETTERS
    // =========================
    public void setEmbedding(float[] embedding) { this.embedding = embedding; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public void setResponse(String response) { this.response = response; }
    public void setQuery(String query) { this.query = query; }
}