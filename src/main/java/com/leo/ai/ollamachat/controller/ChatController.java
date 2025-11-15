package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.dto.ChatRequest;
import com.leo.ai.ollamachat.dto.ChatResponse;
import com.leo.ai.ollamachat.model.ChatMessage;
import com.leo.ai.ollamachat.repository.ChatMessageRepository;
import com.leo.ai.ollamachat.service.OllamaService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final OllamaService ollamaService;
    private final ChatMessageRepository chatRepository;

    public ChatController(OllamaService ollamaService, ChatMessageRepository chatRepository) {
        this.ollamaService = ollamaService;
        this.chatRepository = chatRepository;
    }

    /**
     * Endpoint de teste — diagnóstico rápido.
     */
    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("message", "✅ Backend está respondendo corretamente.");
    }

    /**
     * Endpoint tradicional — resposta completa (sem streaming).
     */
    @PostMapping
    public Mono<ChatResponse> chat(@RequestBody ChatRequest request) {
        String userMessage = request.getMessage();

        if (userMessage == null || userMessage.isBlank()) {
            return Mono.just(new ChatResponse("⚠️ A mensagem não pode estar vazia."));
        }

        return ollamaService.askOllama(userMessage)
                .flatMap(responseMap -> {
                    String botResponse = extrairResposta(responseMap);

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setUserMessage(userMessage);
                    chatMessage.setBotResponse(botResponse);
                    chatMessage.setTimestamp(LocalDateTime.now());

                    return chatRepository.save(chatMessage)
                            .thenReturn(new ChatResponse(botResponse));
                })
                .onErrorResume(e -> Mono.just(new ChatResponse("❌ Erro: " + e.getMessage())));
    }

    /**
     * Endpoint streaming — resposta token a token.
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        String prompt = request.getMessage();
        return ollamaService.streamOllamaResponse(prompt)
                .onErrorResume(e -> Flux.just("❌ Erro no streaming: " + e.getMessage()));
    }

    /**
     * Extrai o texto principal retornado pelo Ollama.
     */
    private String extrairResposta(Map<String, Object> responseMap) {
        if (responseMap == null) return "⚠️ Resposta vazia do Ollama.";

        Object raw = responseMap.get("response");
        if (raw == null) raw = responseMap.get("message");
        if (raw == null) raw = responseMap.get("output");
        if (raw == null) raw = responseMap.get("content");

        return raw != null ? raw.toString() : "⚠️ Erro ao processar resposta do modelo.";
    }
}
