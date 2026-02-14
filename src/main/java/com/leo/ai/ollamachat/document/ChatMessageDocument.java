package com.leo.ai.ollamachat.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "chat_history")
public class ChatMessageDocument {

    @Id
    private String id;
    private String sessionId;
    private String prompt;
    private String response;
    private Instant createdAt;

    // getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSessionId() {return sessionId;}
    public void setSessionId(String sessionId) {this.sessionId = sessionId;}

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getResponse() { return response; }
    public void setResponse(String response) { this.response = response; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
