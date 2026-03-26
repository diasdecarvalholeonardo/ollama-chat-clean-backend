package com.leo.ai.ollamachat.ingestion.controller;

import com.leo.ai.ollamachat.ingestion.dto.IngestionRequest;
import com.leo.ai.ollamachat.ingestion.dto.IngestionResponse;
import com.leo.ai.ollamachat.ingestion.dto.IngestionTextRequest;
import com.leo.ai.ollamachat.ingestion.service.KnowledgeIngestionService;
import com.leo.ai.ollamachat.ingestion.service.IngestionService;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ingestion")
//@Profile("prod")
public class IngestionController {

    private final KnowledgeIngestionService ingestionService;
    private final IngestionService quickIngestionService;

    public IngestionController(
            KnowledgeIngestionService ingestionService,
            IngestionService quickIngestionService
    ) {
        this.ingestionService = ingestionService;
        this.quickIngestionService = quickIngestionService;
    }

    /**
     * 🟢 INGESTÃO COMPLETA (SEU FLUXO ORIGINAL)
     * Suporta PDF, WEB, TEXT estruturado
     */
    @PostMapping
    public IngestionResponse ingest(
            @RequestBody IngestionRequest request
    ) {

        ingestionService.ingest(request);

        return new IngestionResponse(
                "OK",
                "Document ingested successfully"
        );
    }

    /**
     * 🔵 INGESTÃO RÁPIDA (NOVO)
     * Usado para testes rápidos de RAG
     */
    @PostMapping("/text")
    public String ingestText(
            @RequestBody IngestionTextRequest request
    ) {

        quickIngestionService.ingestText(request.getContent());

        return "OK";
    }
}