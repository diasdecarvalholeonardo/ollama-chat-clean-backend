package com.leo.ai.ollamachat.retrieval.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.domain.document.DocumentChunkRepository;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class KeywordSearchService {

    private final DocumentChunkRepository repository;

    public KeywordSearchService(DocumentChunkRepository repository) {
        this.repository = repository;
    }

    public List<DocumentChunk> search(String query, int topK) {

        List<DocumentChunk> chunks = repository.findAll();

        return chunks.stream()
                .sorted((a, b) -> Double.compare(
                        keywordScore(b.getContent(), query),
                        keywordScore(a.getContent(), query)
                ))
                .limit(topK)
                .toList();
    }

    private double keywordScore(String text, String query) {

        text = text.toLowerCase();
        query = query.toLowerCase();

        double score = 0;

        for (String term : query.split("\\s+")) {

            if (text.contains(term)) {
                score += 1;
            }
        }

        return score;
    }
}
