package com.leo.ai.ollamachat.knowledge.repository;

import com.leo.ai.ollamachat.knowledge.entity.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeDocumentRepository
        extends JpaRepository<KnowledgeDocument, String> {
}
