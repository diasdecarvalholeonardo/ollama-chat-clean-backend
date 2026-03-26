package com.leo.ai.ollamachat.embedding.repository;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class VectorSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Hybrid Search usando RRF (Reciprocal Rank Fusion)
     *
     * Combina:
     * - busca vetorial (pgvector)
     * - full-text search (PostgreSQL)
     *
     * Estratégia:
     * - gera ranking vetorial
     * - gera ranking textual
     * - aplica RRF para fusão dos rankings
     */
    @SuppressWarnings("unchecked")
    public List<DocumentChunk> search(float[] embedding, String query, int limit) {

        if (embedding == null || embedding.length == 0) {
            throw new IllegalArgumentException("Embedding cannot be empty");
        }

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be empty");
        }

        String vector = toPgVector(embedding);

        String sql = """
        WITH
        vector_results AS (
            SELECT
                id,
                ROW_NUMBER() OVER (
                    ORDER BY embedding <-> CAST(:embedding AS vector)
                ) AS rank_vector
            FROM agent_knowledge_base
            LIMIT 50
        ),
        text_results AS (
            SELECT
                id,
                ROW_NUMBER() OVER (
                    ORDER BY
                        ts_rank_cd(content_tsv, plainto_tsquery('portuguese', :query)) +
                        ts_rank_cd(content_tsv, plainto_tsquery('english', :query))
                    DESC
                ) AS rank_text
            FROM agent_knowledge_base
            LIMIT 50
        ),
        fused AS (
            SELECT
                a.id,
                (1.0 / (60 + COALESCE(v.rank_vector, 1000))) +
                (1.0 / (60 + COALESCE(t.rank_text, 1000))) AS score
            FROM agent_knowledge_base a
            LEFT JOIN vector_results v ON a.id = v.id
            LEFT JOIN text_results t ON a.id = t.id
            WHERE v.rank_vector IS NOT NULL
               OR t.rank_text IS NOT NULL
        )
        SELECT akb.*
        FROM agent_knowledge_base akb
        JOIN fused f ON akb.id = f.id
        ORDER BY f.score DESC
        LIMIT :limit
        """;

        return entityManager
                .createNativeQuery(sql, DocumentChunk.class)
                .setParameter("embedding", vector)
                .setParameter("query", query)
                .setParameter("limit", limit)
                .getResultList();
    }

    /**
     * Converte float[] para formato pgvector
     *
     * Exemplo:
     * [0.12,0.33,0.91]
     */
    private String toPgVector(float[] vector) {

        return "[" +
                IntStream.range(0, vector.length)
                        .mapToObj(i -> Float.toString(vector[i]))
                        .collect(Collectors.joining(",")) +
                "]";
    }
}