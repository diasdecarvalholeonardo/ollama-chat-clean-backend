package com.leo.ai.ollamachat.ingestion.service;

import com.leo.ai.ollamachat.ingestion.dto.IngestionRequest;

public interface KnowledgeIngestionService {

    void ingest(IngestionRequest request);
}

