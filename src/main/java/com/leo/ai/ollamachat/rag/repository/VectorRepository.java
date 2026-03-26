package com.leo.ai.ollamachat.rag.repository;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VectorRepository extends JpaRepository<DocumentChunk, Long> {

    /**
     * ✅ USAR AGORA (pgvector compatível)
     */
    @Query(value = """
        SELECT *
        FROM document_chunks
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<DocumentChunk> searchSimilar(
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );

    /**
     * ⚠️ FUTURO (quando usar conversor JPA)
     */
    @Query(value = """
        SELECT *
        FROM document_chunks
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<DocumentChunk> searchSimilarRaw(
            @Param("embedding") float[] embedding,
            @Param("limit") int limit
    );
}