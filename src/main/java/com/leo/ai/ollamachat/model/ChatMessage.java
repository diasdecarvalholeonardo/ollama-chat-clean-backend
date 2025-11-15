package com.leo.ai.ollamachat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Representa uma mensagem no histórico do chat entre o usuário e o modelo.
 * Mantém compatibilidade com campos antigos e adiciona timestamp automático.
 */
@Document(collection = "chat_messages")
public class ChatMessage {

    @Id
    private String id;

    // Nome mantido igual para compatibilidade com versões anteriores
    private String userMessage;

    private String botResponse;

    // Timestamp automático caso não seja informado
    private LocalDateTime timestamp = LocalDateTime.now();

    public ChatMessage() {}

    public ChatMessage(String id, String userMessage, String botResponse, LocalDateTime timestamp) {
        this.id = id;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.timestamp = (timestamp != null) ? timestamp : LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        this.timestamp = (timestamp != null) ? timestamp : LocalDateTime.now();
    }
}
