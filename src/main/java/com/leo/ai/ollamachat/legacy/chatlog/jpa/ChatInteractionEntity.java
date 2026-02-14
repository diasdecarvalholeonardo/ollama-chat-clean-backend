package com.leo.ai.ollamachat.legacy.chatlog.jpa;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_interaction")
public class ChatInteractionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, length = 100)
    private String sessionId;

    @Column(name = "user_message", columnDefinition = "TEXT", nullable = false)
    private String userMessage;

    @Column(name = "assistant_message", columnDefinition = "TEXT", nullable = false)
    private String assistantMessage;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Construtor obrigatório para JPA
    protected ChatInteractionEntity() {}

    public ChatInteractionEntity(String sessionId, String userMessage, String assistantMessage, LocalDateTime createdAt) {
        this.sessionId = sessionId;
        this.userMessage = userMessage;
        this.assistantMessage = assistantMessage;
        this.createdAt = createdAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getSessionId() { return sessionId; }
    public String getUserMessage() { return userMessage; }
    public String getAssistantMessage() { return assistantMessage; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
