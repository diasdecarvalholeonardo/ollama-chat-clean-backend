package com.leo.ai.ollamachat.agent.context.provider.mcp;

import java.util.List;

public record McpContextResponseDto(
        String id,
        String type,
        String source,
        McpData data
) {
    public record McpData(
            List<String> rules,
            List<String> constraints
    ) {}
}

