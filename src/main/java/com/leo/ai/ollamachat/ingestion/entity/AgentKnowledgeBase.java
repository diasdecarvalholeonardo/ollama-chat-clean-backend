package com.leo.ai.ollamachat.ingestion.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agent_knowledge_base")
public class AgentKnowledgeBase {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    /**
     * Metadata flexível (JSONB)
     * Exemplo:
     * {
     *   "title": "Spring Boot Guide",
     *   "author": "Pivotal",
     *   "section": "Auto Configuration"
     * }
     */
    @Column(columnDefinition = "jsonb")
    private String metadata;

    /**
     * Embedding vetorial (pgvector)
     */
    @Column(columnDefinition = "vector(384)")
    private float[] embedding;

    /**
     * Documento de origem
     * Ex: spring-guide.pdf
     */
    @Column(name = "source_document")
    private String sourceDocument;

    /**
     * Página do documento
     */
    @Column(name = "page_number")
    private Integer pageNumber;

    /**
     * Tipo da fonte (pdf, markdown, web, etc)
     */
    @Column(name = "source_type")
    private String sourceType;

    /**
     * URL opcional da fonte
     */
    @Column(name = "source_url")
    private String sourceUrl;

    /* =========================
       Getters & Setters
       (todos preservados)
       ========================= */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public float[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    public String getSourceDocument() {
        return sourceDocument;
    }

    public void setSourceDocument(String sourceDocument) {
        this.sourceDocument = sourceDocument;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}