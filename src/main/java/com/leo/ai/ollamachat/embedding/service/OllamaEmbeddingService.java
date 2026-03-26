package com.leo.ai.ollamachat.embedding.service;

import com.leo.ai.ollamachat.embedding.cache.EmbeddingCacheService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OllamaEmbeddingService implements EmbeddingService {

    private static final int DEFAULT_DIMENSION = 768;

    private final EmbeddingModel embeddingModel;
    private final EmbeddingCacheService cacheService;

    public OllamaEmbeddingService(EmbeddingModel embeddingModel,
                                  EmbeddingCacheService cacheService) {
        this.embeddingModel = embeddingModel;
        this.cacheService = cacheService;
    }

    @Override
    public float[] embed(String text) {

        validateInput(text);

        return cacheService.get(text)
                .orElseGet(() -> {

                    try {
                        EmbeddingResponse response =
                                embeddingModel.embedForResponse(List.of(text));

                        float[] embedding = extractFirst(response);

                        cacheService.save(text, embedding);

                        return embedding;

                    } catch (Exception e) {
                        return fallbackVector();
                    }
                });
    }

    @Override
    public float[] generateEmbedding(String text) {
        return embed(text);
    }

    @Override
    public List<float[]> generateEmbeddings(List<String> texts) {

        if (texts == null || texts.isEmpty()) {
            return List.of();
        }

        try {
            EmbeddingResponse response =
                    embeddingModel.embedForResponse(texts);

            return extractAll(response);

        } catch (Exception e) {

            List<float[]> fallbackResults = new ArrayList<>();

            for (String text : texts) {
                fallbackResults.add(embed(text)); // já usa cache
            }

            return fallbackResults;
        }
    }

    // =========================
    // 🔧 MÉTODOS INTERNOS
    // =========================

    private void validateInput(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
    }

    private float[] extractFirst(EmbeddingResponse response) {
        return response.getResults().get(0).getOutput();
    }

    private List<float[]> extractAll(EmbeddingResponse response) {

        List<float[]> results = new ArrayList<>();

        response.getResults()
                .forEach(r -> results.add(r.getOutput()));

        return results;
    }

    private float[] fallbackVector() {
        return new float[DEFAULT_DIMENSION];
    }
}