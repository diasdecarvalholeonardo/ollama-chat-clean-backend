package com.leo.ai.ollamachat.service.ollama.OllamaService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService {

    private final RestClient restClient;

    @Value("${ollama.model:gemma3:1b}")
    private String model;

    public OllamaService(
            @Value("${ollama.base-url:http://localhost:11434}") String baseUrl
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 🔵 Método técnico (baixo nível)
     * Mantido para debug, testes e uso direto
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> askOllamaSync(String prompt) {

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", false);

        try {
            return restClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Erro ao chamar Ollama: " + e.getMessage());
            return error;
        }
    }

    /**
     * 🟢 Método de domínio (USADO PELO CHAT)
     * Retorna APENAS o texto do modelo
     */
    public String generate(String prompt) {

        Map<String, Object> response = askOllamaSync(prompt);

        if (response == null) {
            return "Erro: resposta nula do Ollama.";
        }

        if (response.containsKey("error")) {
            return response.get("error").toString();
        }

        Object text = response.get("response");

        return text != null
                ? text.toString()
                : "Erro: resposta vazia do modelo.";
    }
}
