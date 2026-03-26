package com.leo.ai.ollamachat.memory.vector.repository;

import com.leo.ai.ollamachat.memory.vector.model.VectorMemory;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VectorMemoryRepository
        extends JpaRepository<VectorMemory, Long> {

    @Query(value = """
        SELECT *
        FROM vector_memory
        ORDER BY embedding <=> CAST(:embedding AS vector)
        LIMIT :limit
        """,
        nativeQuery = true)
    List<VectorMemory> searchSimilar(
            @Param("embedding") String embedding,
            @Param("limit") int limit);

}
