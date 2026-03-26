package com.leo.ai.ollamachat.embedding.cache;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.leo.ai.ollamachat.domain.cache.SemanticCacheEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmbeddingCacheRepository
        extends JpaRepository<EmbeddingCacheEntry, Long> {

    // =========================
    // ✅ JÁ EXISTENTE (INALTERADO)
    // =========================
    Optional<EmbeddingCacheEntry> findByHash(String hash);

    // =========================
    // 🔥 CLEANUP AUTOMÁTICO (MANTIDO)
    // =========================
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM EmbeddingCacheEntry e WHERE e.expiresAt < :now")
    int deleteExpired(@Param("now") LocalDateTime now);

    // =========================
    // 🧠 NOVO — BUSCA SEMÂNTICA (pgvector)
    // =========================
    @Query(value = """
    	    SELECT * FROM embedding_cache
    	    ORDER BY embedding <-> CAST(:embedding AS vector)
    	    LIMIT :k
    	""", nativeQuery = true)
    	List<EmbeddingCacheEntry> findTopKNearest(
    	        @Param("embedding") float[] embedding,
    	        @Param("k") int k
    	);
}
