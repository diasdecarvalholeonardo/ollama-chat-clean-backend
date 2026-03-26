package com.leo.ai.ollamachat.memory.vector.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vector_memory")
public class VectorMemory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sessionId;

    @Column(columnDefinition = "text")
    private String content;

    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    /**
     * Construtor padrão (necessário para JPA)
     */
    public VectorMemory() {}

    /**
     * Construtor usado pelo sistema ao criar memórias
     */
    public VectorMemory(String sessionId, String content, float[] embedding) {
        this.sessionId = sessionId;
        this.content = content;
        this.embedding = embedding;
    }

    /**
     * Getter existente (mantido para compatibilidade)
     */
    public String getContent() {
        return content;
    }

    /**
     * Novos getters necessários para serviços e repositories
     */

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    /**
     * Setters necessários para criação dinâmica de memória
     */

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }
}