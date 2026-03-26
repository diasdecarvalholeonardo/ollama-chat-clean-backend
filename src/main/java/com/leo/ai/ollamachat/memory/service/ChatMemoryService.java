package com.leo.ai.ollamachat.memory.service;

import com.leo.ai.ollamachat.memory.model.ChatMessage;
import com.leo.ai.ollamachat.memory.repository.ChatMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMemoryService {

    private final ChatMemoryRepository repository;

    public ChatMemoryService(ChatMemoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Salva mensagem do usuário
     */
    public void saveUserMessage(String sessionId, String message) {

        ChatMessage chatMessage =
                new ChatMessage(sessionId, "user", message);

        repository.save(chatMessage);

    }

    /**
     * Salva resposta do assistente
     */
    public void saveAssistantMessage(String sessionId, String message) {

        ChatMessage chatMessage =
                new ChatMessage(sessionId, "assistant", message);

        repository.save(chatMessage);

    }

    /**
     * Recupera conversa ordenada cronologicamente
     */
    public List<ChatMessage> getConversation(String sessionId) {

        return repository
                .findBySessionIdOrderByTimestampAsc(sessionId);

    }

    /**
     * Constrói contexto textual da conversa
     * usado no prompt do LLM
     */
    public String buildConversationContext(String sessionId) {

        List<ChatMessage> messages =
                repository.findBySessionIdOrderByTimestampAsc(sessionId);

        StringBuilder context = new StringBuilder();

        for (ChatMessage msg : messages) {

            context
                    .append(msg.getRole())
                    .append(": ")
                    .append(msg.getContent())
                    .append("\n");

        }

        return context.toString();

    }

}