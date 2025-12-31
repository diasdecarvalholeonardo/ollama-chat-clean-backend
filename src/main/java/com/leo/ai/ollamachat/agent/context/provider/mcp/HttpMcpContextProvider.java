package com.leo.ai.ollamachat.agent.context.provider.mcp;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;
import com.leo.ai.ollamachat.agent.context.provider.ContextProvider;

@Component
@Profile("mcp")
public class HttpMcpContextProvider implements ContextProvider {

    private final WebClient webClient;

    public HttpMcpContextProvider(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Override
    public ContextResponse resolve(ContextRequest request) {
        McpContextResponseDto responseDto =
                webClient.post()
                        .uri("/mcp/context")
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(McpContextResponseDto.class)
                        .block();

        if (responseDto == null) {
            throw new IllegalStateException("Resposta vazia do MCP");
        }

        return McpContextMapper.toDomain(responseDto);
    }
}
