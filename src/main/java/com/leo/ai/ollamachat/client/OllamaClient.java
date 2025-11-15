package com.leo.ai.ollamachat.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class OllamaClient {

    private final WebClient webClient;

    public OllamaClient() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:11434") // Servidor local do Ollama
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024)) // 16MB buffer
                        .build())
                .build();
    }

    /**
     * Envia um prompt ao modelo gemma3:1b no Ollama,
     * após verificar se o servidor está disponível.
     */
    public Mono<String> askOllama(String prompt) {
        return checkOllamaStatus()
                .flatMap(isOnline -> {
                    if (!isOnline) {
                        System.err.println("⚠️ [WARN] Ollama não está respondendo no momento.");
                        return Mono.just("⚠️ O servidor Ollama não está disponível. Tente novamente em alguns segundos.");
                    }

                    System.out.println("[DEBUG] Enviando prompt para Ollama: " + prompt);

                    return webClient.post()
                            .uri("/api/generate")
                            .bodyValue(new OllamaRequest("gemma3:1b", prompt))
                            .retrieve()
                            .onStatus(HttpStatusCode::is5xxServerError, res ->
                                    Mono.error(new RuntimeException("Erro interno no Ollama (5xx).")))
                            .onStatus(HttpStatusCode::is4xxClientError, res ->
                                    Mono.error(new RuntimeException("Erro de cliente ao acessar Ollama (4xx).")))
                            .bodyToMono(String.class)
                            .timeout(Duration.ofSeconds(300)) // tempo máximo para resposta
                            .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(5))
                                    .filter(e -> e instanceof WebClientResponseException)
                                    .onRetryExhaustedThrow((r, s) -> s.failure()))
                            .onErrorResume(WebClientResponseException.class, e -> {
                                System.err.println("[ERRO OLLAMA - HTTP] " + e.getStatusText());
                                return Mono.just("Erro HTTP ao acessar Ollama: " + e.getStatusText());
                            })
                            .onErrorResume(Exception.class, e -> {
                                System.err.println("[ERRO OLLAMA - GERAL] " + e.getMessage());
                                return Mono.just("Erro geral ao comunicar com Ollama: " + e.getMessage());
                            });
                });
    }

    /**
     * Verifica se o Ollama está online e se o modelo gemma3:1b está disponível.
     */
    private Mono<Boolean> checkOllamaStatus() {
        System.out.println("[DEBUG] Verificando status do Ollama...");
        return webClient.get()
                .uri("/api/tags")
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    boolean ativo = body.contains("gemma3:1b");
                    System.out.println(ativo
                            ? "[DEBUG] Ollama online e modelo gemma3:1b disponível."
                            : "[DEBUG] Ollama online, mas modelo gemma3:1b não listado.");
                    return ativo;
                })
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> {
                    System.err.println("[ERRO STATUS OLLAMA] " + e.getMessage());
                    return Mono.just(false);
                });
    }

    /**
     * DTO da requisição para o Ollama
     */
    static class OllamaRequest {
        private String model;
        private String prompt;

        public OllamaRequest(String model, String prompt) {
            this.model = model;
            this.prompt = prompt;
        }

        public String getModel() { return model; }
        public String getPrompt() { return prompt; }
    }
}
