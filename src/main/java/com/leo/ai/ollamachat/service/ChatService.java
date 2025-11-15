package com.leo.ai.ollamachat.service;

import com.leo.ai.ollamachat.client.OllamaClient;
import com.leo.ai.ollamachat.dto.ChatRequest;
import com.leo.ai.ollamachat.dto.ChatResponse;
import com.leo.ai.ollamachat.model.ChatHistory;
import com.leo.ai.ollamachat.model.ChatMessage;
import com.leo.ai.ollamachat.repository.ChatHistoryRepository;
import com.leo.ai.ollamachat.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ChatService {

    private final OllamaClient ollamaClient;
    private final ChatMessageRepository mongoRepo;
    private final ChatHistoryRepository pgRepo;

    public ChatService(OllamaClient ollamaClient,
                       ChatMessageRepository mongoRepo,
                       ChatHistoryRepository pgRepo) {
        this.ollamaClient = ollamaClient;
        this.mongoRepo = mongoRepo;
        this.pgRepo = pgRepo;
    }

    public Mono<ChatResponse> processChat(ChatRequest request) {
        return ollamaClient.askOllama(request.getMessage())
                .flatMap(response -> {
                    ChatMessage mongoMsg = new ChatMessage(null, request.getMessage(), response, LocalDateTime.now());
                    ChatHistory pgMsg = new ChatHistory(null, request.getMessage(), response, LocalDateTime.now());

                    Mono<ChatMessage> mongoSave = mongoRepo.save(mongoMsg);
                    Mono<ChatHistory> pgSave = pgRepo.save(pgMsg);

                    return Mono.when(mongoSave, pgSave)
                            .thenReturn(new ChatResponse(response));
                });
    }

}
