package com.leo.ai.ollamachat.cache.response;

import org.springframework.stereotype.Service;

@Service
public class ContextFusionService {

    public String fuse(String query, String memoryContext) {

        if (memoryContext == null || memoryContext.isBlank()) {
            return query;
        }

        return """
                QUERY:
                %s

                CONTEXT:
                %s
                """.formatted(query, memoryContext);
    }
}
