package com.leo.ai.ollamachat.service.ollama;

import com.leo.ai.ollamachat.llm.LLMClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.HashMap;
import java.util.Map;

@Service
public class OllamaService implements LLMClient {

    private final RestClient restClient;

    @Value("${ollama.model:gemma:2b}")
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
        return askOllamaSync(prompt, null);
    }

    /**
     * 🔵 Método técnico com suporte a system prompt
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> askOllamaSync(String prompt, String systemPrompt) {

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("prompt", prompt);
        body.put("stream", false);

        if (systemPrompt != null) {
            body.put("system", systemPrompt);
        }

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
     * 🟢 Método de domínio simples
     */
    @Override
    public String generate(String prompt) {
        return generate(prompt, null);
    }

    /**
     * 🟢 Método principal (com system prompt)
     */
    @Override
    public String generate(String prompt, String systemPrompt) {

        Map<String, Object> response = askOllamaSync(prompt, systemPrompt);

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

    /**
     * 🧠 Método semântico (nível aplicação)
     */
    public String ask(String userMessage) {
        return generate(userMessage, "Você é um assistente útil.");
    }

    /**
     * 🔥 Preparado para RAG
     */
    public String generateWithContext(String question, String context) {

        String systemPrompt = """
                Você é um assistente que responde com base no contexto fornecido.
                Se a resposta não estiver no contexto, diga que não sabe.

                Contexto:
                """ + context;

        return generate(question, systemPrompt);
    }
}