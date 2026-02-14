package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.dto.ChatMessageRequest;
import com.leo.ai.ollamachat.dto.ChatMessageResponse;
import com.leo.ai.ollamachat.service.ChatHistoryService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class ChatHistoryController {

    private final ChatHistoryService service;

    public ChatHistoryController(ChatHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ChatMessageResponse save(
            @RequestBody ChatMessageRequest request
    ) {
        var saved = service.save(request);

        return new ChatMessageResponse(
                saved.getId(),
                saved.getPrompt(),
                saved.getResponse(),
                saved.getCreatedAt()
        );
    }

    @GetMapping("/{sessionId}")
    public Object listBySession(
            @PathVariable String sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return service.findBySession(sessionId, page, size);
    }
}
