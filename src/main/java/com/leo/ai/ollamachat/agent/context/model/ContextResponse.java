package com.leo.ai.ollamachat.agent.context.model;

import java.util.List;

public record ContextResponse(
    List<String> tags,
    String content
) {}

