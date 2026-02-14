package com.leo.ai.ollamachat.ingestion.dto;

public class IngestionResponse {

    private String status;
    private String message;

    // ✅ construtor necessário
    public IngestionResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // opcional, mas recomendado para Jackson
    public IngestionResponse() {
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
