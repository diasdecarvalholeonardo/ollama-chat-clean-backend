package com.leo.ai.ollamachat.ingestion.dto;

import java.util.Map;

public class IngestionRequest {

    private String sourceType;

    private String sourceUri;

    private Map<String, Object> metadata;

    public IngestionRequest() {
    }

    public IngestionRequest(
            String sourceType,
            String sourceUri,
            Map<String, Object> metadata
    ) {
        this.sourceType = sourceType;
        this.sourceUri = sourceUri;
        this.metadata = metadata;
    }

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

    public boolean isPdf() {
        return sourceType != null &&
               sourceType.equalsIgnoreCase("PDF");
    }

    public boolean isWeb() {
        return sourceType != null &&
               sourceType.equalsIgnoreCase("WEB");
    }

    public boolean isText() {
        return sourceType != null &&
               sourceType.equalsIgnoreCase("TEXT");
    }

    public boolean hasMetadata() {
        return metadata != null &&
               !metadata.isEmpty();
    }

    public String getTextContent() {

        if (metadata == null) {
            return null;
        }

        Object text = metadata.get("text");

        return text != null ? text.toString() : null;
    }
}