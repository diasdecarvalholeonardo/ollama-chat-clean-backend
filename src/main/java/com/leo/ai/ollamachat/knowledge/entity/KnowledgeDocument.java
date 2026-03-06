package com.leo.ai.ollamachat.knowledge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "knowledge_document")
public class KnowledgeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String sourceType;

    private String sourceUri;

    private LocalDateTime createdAt;

    public KnowledgeDocument() {
        this.createdAt = LocalDateTime.now();
    }

    public KnowledgeDocument(String content, String sourceType, String sourceUri) {
        this.content = content;
        this.sourceType = sourceType;
        this.sourceUri = sourceUri;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getContent() { return content; }
    public String getSourceType() { return sourceType; }
    public String getSourceUri() { return sourceUri; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setContent(String content) { this.content = content; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public void setSourceUri(String sourceUri) { this.sourceUri = sourceUri; }
}
