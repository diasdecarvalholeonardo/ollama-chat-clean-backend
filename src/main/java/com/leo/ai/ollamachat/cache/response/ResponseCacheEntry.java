package com.leo.ai.ollamachat.cache.response;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "response_cache")
public class ResponseCacheEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String query;

    // 🔥 CONTEXTO (já integrado ao seu modelo)
    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime expiresAt;

    // =========================
    // ✅ CONSTRUTOR PADRÃO (JPA)
    // =========================
    public ResponseCacheEntry() {}

    // =========================
    // ✅ CONSTRUTOR ANTIGO (COMPATIBILIDADE)
    // =========================
    public ResponseCacheEntry(String query, String response, float[] embedding) {
        this.query = query;
        this.response = response;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
    }

    // =========================
    // 🔥 CONSTRUTOR COMPLETO (NOVO PADRÃO)
    // =========================
    public ResponseCacheEntry(String query, String context, String response, float[] embedding) {
        this.query = query;
        this.context = context;
        this.response = response;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
    }

    // =========================
    // 🔥 GETTERS COMPLETOS
    // =========================

    public Long getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public String getContext() {
        return context;
    }

    public String getResponse() {
        return response;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    // =========================
    // 🔥 SETTERS CONTROLADOS
    // =========================

    public void setContext(String context) {
        this.context = context;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    // 🔥 opcional: atualizar resposta (útil para reuso)
    public void setResponse(String response) {
        this.response = response;
    }

    // =========================
    // 🔥 UTILIDADES (NOVO)
    // =========================

    public boolean isExpired() {
        return expiresAt != null && expiresAt.isBefore(LocalDateTime.now());
    }

    public void touch() {
        this.createdAt = LocalDateTime.now();
    }
}