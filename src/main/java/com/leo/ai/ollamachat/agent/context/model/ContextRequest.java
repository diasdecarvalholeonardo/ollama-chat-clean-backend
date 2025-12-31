package com.leo.ai.ollamachat.agent.context.model;

public record ContextRequest(
    String userInput,
    String agentId
) {}

