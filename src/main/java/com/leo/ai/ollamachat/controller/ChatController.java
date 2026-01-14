package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.dto.ChatRequest;
import com.leo.ai.ollamachat.dto.ChatResponse;
import com.leo.ai.ollamachat.model.ChatMessage;
import com.leo.ai.ollamachat.repository.jpa.ChatMessageRepository;
import com.leo.ai.ollamachat.service.OllamaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final OllamaService ollamaService;
    private final ChatMessageRepository chatRepository;

    public ChatController(OllamaService ollamaService,
                          ChatMessageRepository chatRepository) {
        this.ollamaService = ollamaService;
        this.chatRepository = chatRepository;
    }

    /**
     * 🔹 Endpoint de diagnóstico
     */
    @GetMapping("/ping")
    public Map<String, String> ping() {
        return Map.of(
                "status", "OK",
                "message", "Backend está ativo e recebendo requisições!"
        );
    }

    /**
     * 🧪 Endpoint de teste
     */
    @GetMapping("/test")
    public Map<String, String> test() {
        return Map.of("message", "✅ Backend está respondendo corretamente.");
    }

    /**
     * 🤖 Endpoint principal — resposta completa (bloqueante).
     */
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String userMessage = request.getMessage();

        if (userMessage == null || userMessage.isBlank()) {
            return new ChatResponse("⚠️ A mensagem não pode estar vazia.");
        }

        try {
            // Chamada síncrona ao Ollama
            Map<String, Object> responseMap =
                    ollamaService.askOllamaSync(userMessage);

            String botResponse = extrairResposta(responseMap);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserMessage(userMessage);
            chatMessage.setBotResponse(botResponse);
            chatMessage.setTimestamp(LocalDateTime.now());

            chatRepository.save(chatMessage);

            return new ChatResponse(botResponse);

        } catch (Exception e) {
            return new ChatResponse("❌ Erro: " + e.getMessage());
        }
    }

    /**
     * 🔍 Extrai texto retornado pelo Ollama.
     */
    private String extrairResposta(Map<String, Object> responseMap) {

        if (responseMap == null) {
            return "⚠️ Resposta vazia do Ollama.";
        }

        Object raw = responseMap.get("response");
        if (raw == null) raw = responseMap.get("message");
        if (raw == null) raw = responseMap.get("output");
        if (raw == null) raw = responseMap.get("content");

        return raw != null
                ? raw.toString()
                : "⚠️ Erro ao processar resposta do modelo.";
    }
}
