package com.leo.ai.ollamachat.chat.dto;

import java.util.List;

public class ChatResponse {

    private String response;
    private List<String> sources;

    // Construtor principal (usado pelo RAG)
    public ChatResponse(String response, List<String> sources) {
        this.response = response;
        this.sources = sources;
    }

    // Construtor simples (usado para erro ou mensagens simples)
    public ChatResponse(String response) {
        this.response = response;
        this.sources = null;
    }

    public String getResponse() {
        return response;
    }

    public List<String> getSources() {
        return sources;
    }
}