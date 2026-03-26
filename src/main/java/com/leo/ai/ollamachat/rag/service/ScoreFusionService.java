package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScoreFusionService {

    private static final int MAX_DOCS = 5;

    public List<DocumentChunk> fuse(List<DocumentChunk> documents, String query) {

        if (documents == null || documents.isEmpty()) {
            return documents;
        }

        // 🔥 1. CALCULA SCORE FINAL
        for (DocumentChunk doc : documents) {

            Map<String, Object> metadata = parseMetadata(doc.getMetadata());

            double vectorScore = getScore(metadata, "score");
            double rerankScore = getScore(metadata, "rerankScore");
            double recencyScore = recencyScore(metadata);

            double finalScore =
                    (0.6 * vectorScore) +
                    (0.3 * rerankScore) +
                    (0.1 * recencyScore);

            // 🔥 KEYWORD BOOST (leve e seguro)
            if (containsQuery(doc.getContent(), query)) {
                finalScore += 0.15;
            }

            metadata.put("finalScore", finalScore);

            // 🔥 salva de volta no formato string (compatível com seu banco)
            doc.setMetadata(writeMetadata(metadata));
        }

        // 🔥 2. ORDENA
        List<DocumentChunk> sorted = documents.stream()
                .sorted(Comparator.comparingDouble(
                        d -> -getScore(parseMetadata(d.getMetadata()), "finalScore")
                ))
                .collect(Collectors.toList());

        // 🔥 3. DIVERSITY PENALTY (anti redundância)
        List<DocumentChunk> result = new ArrayList<>();

        for (DocumentChunk doc : sorted) {

            boolean similar = result.stream()
                    .anyMatch(existing -> isSimilar(doc.getContent(), existing.getContent()));

            if (!similar) {
                result.add(doc);
            }

            if (result.size() >= MAX_DOCS) break;
        }

        return result;
    }

    // ================= HELPERS =================

    private double getScore(Map<String, Object> metadata, String key) {
        if (metadata == null) return 0.0;

        Object value = metadata.get(key);

        return value instanceof Number
                ? ((Number) value).doubleValue()
                : 0.0;
    }

    private double recencyScore(Map<String, Object> metadata) {

        Object ts = metadata.get("timestamp");

        if (!(ts instanceof Number)) return 0.0;

        long now = System.currentTimeMillis();
        long docTime = ((Number) ts).longValue();

        long age = now - docTime;

        // 🔥 decaimento exponencial (melhor que linear)
        return Math.exp(-age / (1000.0 * 60 * 60 * 24));
    }

    private boolean containsQuery(String content, String query) {

        if (content == null || query == null) return false;

        return content.toLowerCase().contains(query.toLowerCase());
    }

    private boolean isSimilar(String a, String b) {

        if (a == null || b == null) return false;

        int min = Math.min(a.length(), b.length());

        if (min < 50) return false;

        // 🔥 heurística simples e rápida (produção-friendly)
        return b.contains(a.substring(0, 50));
    }

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
                .collect(Collectors.joining(";"));
    }
}