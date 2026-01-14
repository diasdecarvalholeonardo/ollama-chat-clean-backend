package com.leo.ai.ollamachat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private final WebClient webClient;

    @Value("${ollama.model:gemma3:1b}")
    private String model;

    public OllamaService(
            @Value("${ollama.base-url:http://localhost:11434}") String baseUrl
    ) {

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofMinutes(5));

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    /**
     * 🔵 Chamada síncrona (bloqueante) ao Ollama
     * Usada pelo ChatController (Spring MVC)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> askOllamaSync(String prompt) {

        if (prompt == null || prompt.isBlank()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "⚠️ Prompt vazio.");
            return error;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", false);

        try {
            return webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block(); // 🔥 bloqueio controlado e intencional

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "❌ Erro ao chamar Ollama: " + e.getMessage());
            return error;
        }
    }
}
