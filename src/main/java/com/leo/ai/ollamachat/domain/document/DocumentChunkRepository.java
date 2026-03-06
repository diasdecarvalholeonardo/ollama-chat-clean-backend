package com.leo.ai.ollamachat.domain.document;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    @Query(value = """
        SELECT * FROM document_chunks
        ORDER BY embedding <-> :embedding
        LIMIT :limit
        """, nativeQuery = true)
    List<DocumentChunk> findTopSimilar(
        @Param("embedding") float[] embedding,
        @Param("limit") int limit
    );

}