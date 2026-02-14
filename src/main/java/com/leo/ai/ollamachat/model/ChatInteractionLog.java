package com.leo.ai.ollamachat.model;

import java.time.Instant;

/**
 * DTO PURO — NÃO É ENTITY
 */
public class ChatInteractionLog {

    private String id;
    private String sessionId;
    private String userMessage;
    private String assistantResponse;
    private Instant timestamp;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getAssistantResponse() { return assistantResponse; }
    public void setAssistantResponse(String assistantResponse) { this.assistantResponse = assistantResponse; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
