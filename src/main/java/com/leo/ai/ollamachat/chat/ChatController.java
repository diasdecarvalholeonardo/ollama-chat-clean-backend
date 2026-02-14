package com.leo.ai.ollamachat.chat;

import com.leo.ai.ollamachat.chat.ChatRequest;
import com.leo.ai.ollamachat.chat.ChatResponse;
import com.leo.ai.ollamachat.chat.ChatService;
import com.leo.ai.ollamachat.document.ChatMessageDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Profile;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@Profile("prod")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 🔹 Endpoint de diagnóstico
     */
    @GetMapping("/ping")
    public Object ping() {
        return java.util.Map.of(
                "status", "OK",
                "message", "Backend está ativo e recebendo requisições!"
        );
    }

    /**
     * 🧪 Endpoint de teste
     */
    @GetMapping("/test")
    public Object test() {
        return java.util.Map.of(
                "message", "✅ Backend está respondendo corretamente."
        );
    }

    /**
     * 🤖 Endpoint principal — Chat completo
     */
    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return new ChatResponse("⚠️ A mensagem não pode estar vazia.");
        }

        try {
            var savedInteraction = chatService.chat(request);

            return new ChatResponse(
                    savedInteraction.getResponse()
            );

        } catch (Exception e) {
            return new ChatResponse("❌ Erro: " + e.getMessage());
        }
    }

}
