package com.leo.ai.ollamachat.embedding.service;

import java.util.List;

public interface EmbeddingService {

    float[] embed(String text);

    float[] generateEmbedding(String text);

    List<float[]> generateEmbeddings(List<String> texts);

}
