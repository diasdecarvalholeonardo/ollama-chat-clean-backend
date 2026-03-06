package com.leo.ai.ollamachat.rag.controller;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;
import com.leo.ai.ollamachat.rag.service.VectorSearchService;

import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class VectorSearchTestController {

    private final VectorSearchService service;

    public VectorSearchTestController(VectorSearchService service) {
        this.service = service;
    }

    @GetMapping("/debug/vector-search")
    public List<Document> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int k
    ) {
        return service.search(q, k);
    }
}

