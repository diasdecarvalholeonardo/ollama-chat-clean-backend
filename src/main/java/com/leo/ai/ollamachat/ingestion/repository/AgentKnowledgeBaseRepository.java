package com.leo.ai.ollamachat.ingestion.repository;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AgentKnowledgeBaseRepository
        extends JpaRepository<AgentKnowledgeBase, UUID> {

    @Query(value = """
        SELECT *
        FROM agent_knowledge_base
        ORDER BY embedding <-> CAST(:embedding AS vector)
        LIMIT :limit
        """, nativeQuery = true)
    List<AgentKnowledgeBase> findTopKSimilar(
            @Param("embedding") float[] embedding,
            @Param("limit") int limit
    );
}


