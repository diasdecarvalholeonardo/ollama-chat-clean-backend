package com.leo.ai.ollamachat.chat;

import com.leo.ai.ollamachat.document.ChatMessageDocument;
import com.leo.ai.ollamachat.chat.ChatRequest;
import com.leo.ai.ollamachat.service.ChatHistoryService;
import com.leo.ai.ollamachat.service.OllamaService;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatService {

    private final ChatHistoryService historyService;
    private final OllamaService ollamaService;

    public ChatService(
            ChatHistoryService historyService,
            OllamaService ollamaService
    ) {
        this.historyService = historyService;
        this.ollamaService = ollamaService;
    }

    public ChatMessageDocument chat(ChatRequest request) {

        // 1️⃣ contexto
        String context = historyService.buildContext(
                request.getSessionId(),
                10
        );

        // 2️⃣ prompt final
        String finalPrompt =
                context +
                "User: " + request.getMessage() +
                "\nAssistant:";

        // 3️⃣ chamada ao LLM
        String response = ollamaService.generate(finalPrompt);

        // 4️⃣ persistência
        ChatMessageDocument doc = new ChatMessageDocument();
        doc.setSessionId(request.getSessionId());
        doc.setPrompt(request.getMessage());
        doc.setResponse(response);
        doc.setCreatedAt(Instant.now());

        return historyService.save(doc);
    }
}
