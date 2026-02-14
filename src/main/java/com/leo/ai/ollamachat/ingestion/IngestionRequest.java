package com.leo.ai.ollamachat.ingestion;

import java.util.Map;

public class IngestionRequest {

    private String sourceType; // PDF, WEB, TEXT
    private String sourceUri;  // caminho ou URL
    private Map<String, Object> metadata;

    // getters e setters
}

