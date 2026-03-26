package com.leo.ai.ollamachat.repository.jpa.cache;

import com.leo.ai.ollamachat.domain.cache.SemanticCacheEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SemanticCacheRepository extends JpaRepository<SemanticCacheEntry, Long> {

    // =====================================================
    // 🔥 TOP-1 (COMPATÍVEL COM CÓDIGO ANTIGO)
    // =====================================================
    @Query(value = """
        SELECT *
        FROM semantic_cache
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT 1
        """, nativeQuery = true)
    List<SemanticCacheEntry> findTop1Similar(
            @Param("embedding") String embedding
    );

    // =====================================================
    // 🔥 TOP-K (VERSÃO PRODUÇÃO)
    // =====================================================
    @Query(value = """
        SELECT *
        FROM semantic_cache
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<SemanticCacheEntry> findTopKNearest(
            @Param("embedding") String embedding,
            @Param("limit") int limit
    );
}
