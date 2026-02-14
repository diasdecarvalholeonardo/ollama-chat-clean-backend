package com.leo.ai.ollamachat.dto;

import java.time.Instant;

public class ChatMessageResponse {

    private String id;
    private String prompt;
    private String response;
    private Instant createdAt;

    public ChatMessageResponse(
            String id,
            String prompt,
            String response,
            Instant createdAt
    ) {
        this.id = id;
        this.prompt = prompt;
        this.response = response;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getResponse() {
        return response;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

