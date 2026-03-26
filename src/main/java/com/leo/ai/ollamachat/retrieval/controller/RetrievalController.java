package com.leo.ai.ollamachat.retrieval.controller;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.rag.service.VectorSearchService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/retrieval")
public class RetrievalController {

    private final VectorSearchService vectorSearchService;

    public RetrievalController(VectorSearchService vectorSearchService) {
        this.vectorSearchService = vectorSearchService;
    }

    /**
     * Endpoint para testar retrieval
     *
     * Exemplo:
     * GET /retrieval/search?q=5G network&limit=5
     */
    @GetMapping("/search")
    public List<DocumentChunk> search(
            @RequestParam("q") String question,
            @RequestParam(defaultValue = "5") int limit) {

        return vectorSearchService.search(question, limit);
    }
}
