package com.leo.ai.ollamachat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "chat_messages")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage {

    @Id
    private String id;

    // JSON: "prompt"
    @JsonProperty("prompt")
    private String userMessage;

    // JSON: "response"
    @JsonProperty("response")
    private String botResponse;

    private LocalDateTime timestamp;

    // 🔑 Construtor vazio (Spring / Jackson)
    public ChatMessage() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(String userMessage, String botResponse) {
        this.id = UUID.randomUUID().toString();
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.timestamp = LocalDateTime.now();
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getBotResponse() {
        return botResponse;
    }

    public void setBotResponse(String botResponse) {
        this.botResponse = botResponse;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }
}
