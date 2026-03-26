package com.leo.ai.ollamachat.cache.response;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ResponseCacheRepository
        extends JpaRepository<ResponseCacheEntry, Long> {

    @Query(value = """
        SELECT * FROM response_cache
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT :k
    """, nativeQuery = true)
    List<ResponseCacheEntry> findTopK(
            @Param("embedding") float[] embedding,
            @Param("k") int k
    );

    @Modifying
    @Query("DELETE FROM ResponseCacheEntry e WHERE e.expiresAt < :now")
    int deleteExpired(@Param("now") LocalDateTime now);
}
