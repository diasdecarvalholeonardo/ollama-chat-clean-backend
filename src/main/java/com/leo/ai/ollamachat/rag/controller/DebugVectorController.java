package com.leo.ai.ollamachat.rag.controller;

import org.springframework.web.bind.annotation.*;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.rag.service.VectorSearchService;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugVectorController {

    private final VectorSearchService vectorSearchService;

    public DebugVectorController(VectorSearchService vectorSearchService) {
        this.vectorSearchService = vectorSearchService;
    }

    @GetMapping("/vector-search")
    public List<DocumentChunk> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "3") int topK
    ) {

        return vectorSearchService.search(query, topK);
    }
}
