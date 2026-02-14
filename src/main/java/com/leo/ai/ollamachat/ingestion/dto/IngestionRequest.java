package com.leo.ai.ollamachat.ingestion.dto;

import java.util.Map;

public class IngestionRequest {

    /**
     * Tipo da fonte:
     * PDF | WEB | TEXT | IMAGE | VIDEO (extensível)
     */
    private String sourceType;

    /**
     * URI da fonte:
     * - PDF   → caminho local ou volume Docker
     * - WEB   → URL
     * - TEXT  → identificador ou "inline"
     */
    private String sourceUri;

    /**
     * Metadados livres:
     * Ex:
     * {
     *   "sessionId": "abc123",
     *   "language": "pt-BR",
     *   "tags": ["contrato", "jurídico"],
     *   "chunkSize": 800
     * }
     */
    private Map<String, Object> metadata;

    /* =========================
       Getters & Setters
       ========================= */

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceUri() {
        return sourceUri;
    }

    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    /* =========================
       Helpers (NÃO quebram nada)
       ========================= */

    public boolean isPdf() {
        return "PDF".equalsIgnoreCase(sourceType);
    }

    public boolean isWeb() {
        return "WEB".equalsIgnoreCase(sourceType);
    }

    public boolean isText() {
        return "TEXT".equalsIgnoreCase(sourceType);
    }
}


