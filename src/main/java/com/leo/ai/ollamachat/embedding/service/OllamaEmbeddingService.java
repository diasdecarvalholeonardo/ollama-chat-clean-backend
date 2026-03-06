package com.leo.ai.ollamachat.embedding.service;

import com.leo.ai.ollamachat.embedding.EmbeddingService;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OllamaEmbeddingService implements EmbeddingService {

    private final EmbeddingModel embeddingModel;

    public OllamaEmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public float[] generateEmbedding(String text) {

        EmbeddingResponse response =
                embeddingModel.embedForResponse(List.of(text));

        return response.getResults().get(0).getOutput();
    }

    @Override
    public List<float[]> generateEmbeddings(List<String> texts) {

        EmbeddingResponse response =
                embeddingModel.embedForResponse(texts);

        List<float[]> results = new ArrayList<>();

        response.getResults()
                .forEach(r -> results.add(r.getOutput()));

        return results;
    }
}