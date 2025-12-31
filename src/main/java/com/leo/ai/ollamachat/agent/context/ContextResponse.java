package com.leo.ai.ollamachat.agent.context;

import java.time.Instant;
import java.util.Map;

public class ContextResponse {

    private String contextId;
    private String contextType;
    private String source;
    private Instant timestamp;
    private Map<String, Object> payload;

    public ContextResponse() {
    }

    public ContextResponse(
            String contextId,
            String contextType,
            String source,
            Instant timestamp,
            Map<String, Object> payload
    ) {
        this.contextId = contextId;
        this.contextType = contextType;
        this.source = source;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getContextId() {
        return contextId;
    }

    public String getContextType() {
        return contextType;
    }

    public String getSource() {
        return source;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }
}
