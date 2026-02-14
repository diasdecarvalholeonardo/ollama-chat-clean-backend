package com.leo.ai.ollamachat.chat;

public class ChatResponse {

    private String response;

    // ✅ construtor padrão (Spring/Jackson precisa)
    public ChatResponse() {}

    // ✅ construtor que você está tentando usar
    public ChatResponse(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
