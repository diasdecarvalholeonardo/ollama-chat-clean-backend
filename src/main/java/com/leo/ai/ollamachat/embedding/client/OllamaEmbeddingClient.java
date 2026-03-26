package com.leo.ai.ollamachat.embedding.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Component
public class OllamaEmbeddingClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String OLLAMA_URL = "http://localhost:11434/api/embeddings";

    public List<Double> generateEmbedding(String text) {

        Map<String, Object> request = Map.of(
                "model", "nomic-embed-text",
                "prompt", text
        );

        Map response = restTemplate.postForObject(
                OLLAMA_URL,
                request,
                Map.class
        );

        return (List<Double>) response.get("embedding");
    }
}
