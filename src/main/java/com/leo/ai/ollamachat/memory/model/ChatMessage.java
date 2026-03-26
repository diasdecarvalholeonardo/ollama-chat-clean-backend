package com.leo.ai.ollamachat.memory.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_memory")
public class ChatMessage {

    @Id
    private String id;
    private String sessionId;
    private String role;
    private String content;
    private Instant timestamp;

    public ChatMessage() {}

    public ChatMessage(String sessionId, String role, String content) {
        this.sessionId = sessionId;
        this.role = role;
        this.content = content;
        this.timestamp = Instant.now();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
}
