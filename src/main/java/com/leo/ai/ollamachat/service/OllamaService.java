package com.leo.ai.ollamachat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private final WebClient webClient;

    @Value("${ollama.model:gemma3:1b}")
    private String model;

    public OllamaService(
            @Value("${ollama.base-url:http://localhost:11434}") String baseUrl) {

        // 🔥 AJUSTE CRÍTICO: SEM TIMEOUT DE CONEXÃO E SEM TIMEOUT DE RESPOSTA
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 0)                        // sem timeout de conexão
                .responseTimeout(Duration.ofMillis(Long.MAX_VALUE))                    // sem timeout de resposta
                .keepAlive(true);                                                      // mantém conexão viva

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    /**
     * 🔵 Modo tradicional — resposta completa (sem stream).
     */
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> askOllama(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "⚠️ Nenhum prompt enviado.");
            return Mono.just(err);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", false);

        return webClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (Map<String, Object>) response)
                .onErrorResume(e -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", "❌ Falha ao se comunicar com Ollama: " + e.getMessage());
                    return Mono.just(error);
                });
    }

    /**
     * 🔵 Modo streaming — devolve resposta aos poucos (SSE).
     */
    public Flux<String> streamOllamaResponse(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return Flux.just("⚠️ Nenhum prompt enviado.");
        }

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", true);

        return webClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_NDJSON, MediaType.TEXT_EVENT_STREAM, MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .map(dataBuffer -> {
                    String chunk = dataBuffer.toString(StandardCharsets.UTF_8);
                    org.springframework.core.io.buffer.DataBufferUtils.release(dataBuffer);

                    // 🔍 Extrai incrementalmente o texto de cada JSON recebido
                    if (chunk.contains("\"response\"")) {
                        return chunk.replaceAll(".*\"response\"\\s*:\\s*\"", "")
                                .replaceAll("\".*", "")
                                .replace("\\n", "\n");
                    } else {
                        return "";
                    }
                })
                .filter(line -> !line.isBlank())
                .onErrorResume(e -> Flux.just("❌ Erro no stream: " + e.getMessage()));
    }
}
