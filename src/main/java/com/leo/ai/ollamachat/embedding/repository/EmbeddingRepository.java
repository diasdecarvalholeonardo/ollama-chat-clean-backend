package com.leo.ai.ollamachat.embedding.repository;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmbeddingRepository extends JpaRepository<DocumentChunk, Long> {
}
