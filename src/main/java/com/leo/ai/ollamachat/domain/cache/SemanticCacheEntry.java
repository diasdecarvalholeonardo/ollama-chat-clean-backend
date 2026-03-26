package com.leo.ai.ollamachat.domain.cache;

import jakarta.persistence.*;

@Entity
@Table(name = "semantic_cache")
public class SemanticCacheEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String query;      // antes era "question"

    @Column(columnDefinition = "TEXT")
    private String response;   // antes era "answer"

    // vetor de embedding armazenado no pgvector
    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    // =========================
    // Construtores
    // =========================
    public SemanticCacheEntry() {
    }

    public SemanticCacheEntry(String query, String response, float[] embedding) {
        this.query = query;
        this.response = response;
        this.embedding = embedding;
    }

    // =========================
    // Getters
    // =========================
    public Long getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public String getResponse() {
        return response;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    // =========================
    // Setters (úteis para JPA)
    // =========================
    public void setId(Long id) {
        this.id = id;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }
}