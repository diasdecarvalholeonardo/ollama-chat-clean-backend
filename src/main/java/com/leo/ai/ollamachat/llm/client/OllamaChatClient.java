package com.leo.ai.ollamachat.llm.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
public class OllamaChatClient {

    private final RestTemplate restTemplate;

    @Value("${ollama.api.url:http://localhost:11434/api/generate}")
    private String ollamaUrl;

    @Value("${ollama.model:llama3}")
    private String model;

    public OllamaChatClient() {
        this.restTemplate = new RestTemplate();
    }

    public String chat(String prompt) {

        Map<String, Object> body = Map.of(
                "model", model,
                "prompt", prompt,
                "stream", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(ollamaUrl, request, Map.class);

        return (String) response.getBody().get("response");
    }
}
