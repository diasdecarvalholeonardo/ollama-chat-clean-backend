package com.leo.ai.ollamachat.agent.infra.observability;

import java.time.Instant;

public record TraceContext(
    String traceId,
    Instant startTime
) {}
