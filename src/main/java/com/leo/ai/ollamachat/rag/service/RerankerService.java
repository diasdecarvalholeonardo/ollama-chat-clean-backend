package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RerankerService {

    public List<DocumentChunk> rerank(String query, List<DocumentChunk> chunks, int topK) {

        List<String> terms = tokenize(query);

        Map<String, Double> idf = computeIdf(terms, chunks);

        return chunks.stream()
                .sorted((a, b) -> Double.compare(
                        score(b.getContent(), terms, idf),
                        score(a.getContent(), terms, idf)
                ))
                .limit(topK)
                .toList();
    }

    private double score(String text, List<String> terms, Map<String, Double> idf) {

        List<String> tokens = tokenize(text);

        double score = 0.0;

        for (String term : terms) {

            long tf = tokens.stream()
                    .filter(t -> t.equals(term))
                    .count();

            double tfNormalized = (double) tf / tokens.size();

            double idfValue = idf.getOrDefault(term, 0.0);

            score += tfNormalized * idfValue;
        }

        return score;
    }

    private Map<String, Double> computeIdf(List<String> terms, List<DocumentChunk> chunks) {

        Map<String, Double> idf = new HashMap<>();

        int totalDocs = chunks.size();

        for (String term : terms) {

            int docsContainingTerm = 0;

            for (DocumentChunk chunk : chunks) {

                if (chunk.getContent().toLowerCase().contains(term)) {
                    docsContainingTerm++;
                }
            }

            double value = Math.log((double) (totalDocs + 1) / (docsContainingTerm + 1)) + 1;

            idf.put(term, value);
        }

        return idf;
    }

    private List<String> tokenize(String text) {

        return Arrays.stream(text
                        .toLowerCase()
                        .replaceAll("[^a-zA-Z0-9 ]", "")
                        .split("\\s+"))
                .filter(t -> t.length() > 2)
                .collect(Collectors.toList());
    }
}