package com.leo.ai.ollamachat.client;

/**
 * Representa a requisição enviada ao endpoint /api/generate do Ollama.
 * Parâmetros adicionais foram incluídos para otimizar tempo de resposta
 * e evitar timeouts durante o uso do WebClient.
 */
public class OllamaRequest {
    private String model;       // Nome do modelo (ex: "gemma3:4b")
    private String prompt;      // Texto da pergunta ou comando
    private int num_predict;    // Limite máximo de tokens gerados
    private boolean stream;     // Define se a resposta será transmitida em fluxo (streaming)

    public OllamaRequest() {}

    /**
     * Construtor padrão com otimizações de performance.
     * @param model  Nome do modelo (ex: "gemma3:4b")
     * @param prompt Texto do prompt a ser enviado ao modelo
     */
    public OllamaRequest(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
        this.num_predict = 50;  // reduz a resposta para ~1-2 frases
        this.stream = false;    // desativa streaming para evitar timeout
    }

    // Getters e Setters

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getNum_predict() {
        return num_predict;
    }

    public void setNum_predict(int num_predict) {
        this.num_predict = num_predict;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}

