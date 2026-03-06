package com.leo.ai.ollamachat.retrieval.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.retrieval.service.RetrievalService;

@RestController
public class RetrievalTestController {

    private final RetrievalService retrievalService;

    public RetrievalTestController(RetrievalService retrievalService) {
        this.retrievalService = retrievalService;
    }

    @GetMapping("/test/search")
    public List<DocumentChunk> testSearch(
            @RequestParam String query) {

        return retrievalService.searchSimilar(query, 5);
    }
}
