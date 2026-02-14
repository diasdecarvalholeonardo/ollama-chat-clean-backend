package com.leo.ai.ollamachat.ingestion.controller;

import com.leo.ai.ollamachat.ingestion.dto.IngestionRequest;
import com.leo.ai.ollamachat.ingestion.dto.IngestionResponse;
import com.leo.ai.ollamachat.ingestion.service.KnowledgeIngestionService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingestion")
@Profile("prod")
public class IngestionController {

    private final KnowledgeIngestionService ingestionService;

    public IngestionController(
            KnowledgeIngestionService ingestionService
    ) {
        this.ingestionService = ingestionService;
    }

    @PostMapping
    public IngestionResponse ingest(
            @RequestBody IngestionRequest request
    ) {

        ingestionService.ingest(request);

        return new IngestionResponse(
                "OK",
                "Ingestion request accepted"
        );
    }
}

