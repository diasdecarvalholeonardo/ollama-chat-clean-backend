package com.leo.ai.ollamachat.rag.controller;

import com.leo.ai.ollamachat.rag.service.RagResponse;
import com.leo.ai.ollamachat.rag.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public RagResponse ask(
            @RequestParam String question,
            @RequestParam(defaultValue = "3") int topK,
            @RequestParam(defaultValue = "false") boolean debug
    ) {
        return ragService.ask(question, topK, debug);
    }
}

