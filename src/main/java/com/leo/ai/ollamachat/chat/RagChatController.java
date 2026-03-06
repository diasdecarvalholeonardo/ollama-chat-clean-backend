package com.leo.ai.ollamachat.chat;

import com.leo.ai.ollamachat.rag.service.RagChatService;
import com.leo.ai.ollamachat.chat.dto.ChatRequest;
import com.leo.ai.ollamachat.chat.dto.ChatResponse;
import com.leo.ai.ollamachat.persistence.mongo.chat.ChatMessageDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Profile;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
@Profile({"prod", "docker"})
public class RagChatController {

    private final RagChatService ragChatService;

    public RagChatController(RagChatService ragChatService) {
        this.ragChatService = ragChatService;
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
            return new ChatResponse("⚠️ A mensagem não pode estar vazia.", null);
        }

        try {
            var savedInteraction = ragChatService.chat(request);

            return new ChatResponse(
                    savedInteraction.getResponse()
            );

        } catch (Exception e) {
            return new ChatResponse("❌ Erro: " + e.getMessage());
        }
    }

}
