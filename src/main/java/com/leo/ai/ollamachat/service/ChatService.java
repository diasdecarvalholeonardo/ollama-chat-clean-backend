package com.leo.ai.ollamachat.service;

import com.leo.ai.ollamachat.dto.ChatRequest;
import com.leo.ai.ollamachat.dto.ChatResponse;
import com.leo.ai.ollamachat.model.ChatHistory;
import com.leo.ai.ollamachat.model.ChatMessage;
import com.leo.ai.ollamachat.repository.jpa.ChatHistoryRepository;
import com.leo.ai.ollamachat.repository.jpa.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ChatService {

    private final OllamaService ollamaService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatService(OllamaService ollamaService,
                       ChatMessageRepository chatMessageRepository,
                       ChatHistoryRepository chatHistoryRepository) {

        this.ollamaService = ollamaService;
        this.chatMessageRepository = chatMessageRepository;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public ChatResponse processChat(ChatRequest request) {

        String userMessage = request.getMessage();

        if (userMessage == null || userMessage.isBlank()) {
            return new ChatResponse("⚠️ Mensagem vazia.");
        }

        // 🔹 Chamada síncrona ao Ollama
        Map<String, Object> responseMap =
                ollamaService.askOllamaSync(userMessage);

        String botResponse =
                responseMap.getOrDefault("response", "⚠️ Resposta vazia.").toString();

        LocalDateTime now = LocalDateTime.now();

        // 🔹 Mongo / Histórico rápido
        ChatMessage chatMessage =
                new ChatMessage(null, userMessage, botResponse, now);
        chatMessageRepository.save(chatMessage);

        // 🔹 PostgreSQL / Histórico persistente
        ChatHistory chatHistory =
                new ChatHistory(null, userMessage, botResponse, now);
        chatHistoryRepository.save(chatHistory);

        return new ChatResponse(botResponse);
    }
}
