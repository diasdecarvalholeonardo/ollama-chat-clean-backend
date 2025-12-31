package com.leo.ai.ollamachat.agent.context.provider.mcp;

import com.leo.ai.ollamachat.agent.context.model.ContextResponse;

import java.util.List;

public final class McpContextMapper {

    private McpContextMapper() {}

    public static ContextResponse toDomain(McpContextResponseDto dto) {

        return new ContextResponse(
            List.of(dto.type(), dto.source()),
            dto.data().toString()
        );
    }
}
