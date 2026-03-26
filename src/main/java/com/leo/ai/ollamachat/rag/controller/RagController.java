package com.leo.ai.ollamachat.rag.controller;

import com.leo.ai.ollamachat.chat.dto.ChatRequest;
import com.leo.ai.ollamachat.rag.dto.RagDebugResponse;
import com.leo.ai.ollamachat.rag.service.RagResponse;
import com.leo.ai.ollamachat.rag.service.RagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private static final Logger log = LoggerFactory.getLogger(RagController.class);

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    /**
     * Endpoint GET /api/rag/ask
     * Uso rápido via query string.
     */
    @GetMapping("/ask")
    public RagResponse ask(
            @RequestParam String question,
            @RequestParam(defaultValue = "3") int topK,
            @RequestParam(defaultValue = "false") boolean debug
    ) {
        log.info("RAG ask GET request received: question='{}', topK={}, debug={}", question, topK, debug);
        return ragService.ask(question, topK, debug);
    }

    /**
     * Endpoint POST /api/rag/ask
     * Uso via JSON body (ex: de frontend ou API client)
     */
    @PostMapping("/ask")
    public RagResponse askPost(@RequestBody ChatRequest request,
                               @RequestParam(defaultValue = "3") int topK,
                               @RequestParam(defaultValue = "false") boolean debug) {
        log.info("RAG ask POST request received: message='{}', topK={}, debug={}", request.getMessage(), topK, debug);
        return ragService.ask(request.getMessage(), topK, debug);
    }

    /**
     * Endpoint POST /api/rag/debug
     * Retorna RagDebugResponse completo com métricas, queries geradas, contexto e prompt
     */
    @PostMapping("/debug")
    public RagDebugResponse debug(@RequestBody ChatRequest request) {
        log.info("RAG debug request received: message='{}'", request.getMessage());
        return ragService.debug(request.getMessage());
    }
}