package com.leo.ai.ollamachat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "chat_history")
public class ChatHistory {

    @Id
    private Long id;
    private String userMessage;
    private String botResponse;
    private LocalDateTime timestamp;

    public ChatHistory() {}

    public ChatHistory(Long id, String userMessage, String botResponse, LocalDateTime timestamp) {
        this.id = id;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.timestamp = timestamp;
    }

    // Getters e Setters
}
