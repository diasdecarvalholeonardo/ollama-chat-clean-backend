package com.leo.ai.ollamachat.retrieval.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.rag.service.VectorSearchService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HybridRetrievalService {

    private final VectorSearchService vectorSearchService;
    private final KeywordSearchService keywordSearchService;

    public HybridRetrievalService(
            VectorSearchService vectorSearchService,
            KeywordSearchService keywordSearchService) {

        this.vectorSearchService = vectorSearchService;
        this.keywordSearchService = keywordSearchService;
    }

    public List<DocumentChunk> search(String query, int topK) {

        int fetchSize = topK * 2;

        List<DocumentChunk> vectorResults =
                vectorSearchService.search(query, fetchSize);

        List<DocumentChunk> keywordResults =
                keywordSearchService.search(query, fetchSize);

        Map<Long, DocumentChunk> merged = new HashMap<>();

        // 🔥 1. VECTOR (maior peso)
        for (int i = 0; i < vectorResults.size(); i++) {
            DocumentChunk chunk = vectorResults.get(i);

            double score = 1.0 - (i * 0.01); // ranking baseado na posição

            enrichMetadata(chunk, "vector", score + 0.2);

            merged.put(chunk.getId(), chunk);
        }

        // 🔥 2. KEYWORD (complementar)
        for (int i = 0; i < keywordResults.size(); i++) {
            DocumentChunk chunk = keywordResults.get(i);

            double score = 1.0 - (i * 0.01);

            enrichMetadata(chunk, "keyword", score + 0.1);

            merged.putIfAbsent(chunk.getId(), chunk);
        }

        // 🔥 3. ORDENA POR retrievalScore
        return merged.values().stream()
                .sorted(Comparator.comparingDouble(
                        d -> -getScore(parseMetadata(d.getMetadata()), "retrievalScore")
                ))
                .limit(topK)
                .collect(Collectors.toList());
    }

    // ================= 🔥 ENRIQUECIMENTO =================

    private void enrichMetadata(DocumentChunk chunk, String sourceType, double score) {

        Map<String, Object> metadata = parseMetadata(chunk.getMetadata());

        metadata.put("retrievalSource", sourceType);
        metadata.put("retrievalScore", score);

        chunk.setMetadata(writeMetadata(metadata));
    }

    // ================= 🔧 HELPERS =================

    private Map<String, Object> parseMetadata(String metadataStr) {

        Map<String, Object> map = new HashMap<>();

        if (metadataStr == null || metadataStr.isBlank()) {
            return map;
        }

        String[] entries = metadataStr.split(";");

        for (String entry : entries) {
            String[] kv = entry.split("=");

            if (kv.length == 2) {
                try {
                    map.put(kv[0], Double.parseDouble(kv[1]));
                } catch (Exception e) {
                    map.put(kv[0], kv[1]);
                }
            }
        }

        return map;
    }

    private String writeMetadata(Map<String, Object> map) {
        return map.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + ";" + b)
                .orElse("");
    }

    private double getScore(Map<String, Object> metadata, String key) {

        if (metadata == null) return 0.0;

        Object value = metadata.get(key);

        return value instanceof Number
                ? ((Number) value).doubleValue()
                : 0.0;
    }
}