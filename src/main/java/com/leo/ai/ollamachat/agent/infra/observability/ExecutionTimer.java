package com.leo.ai.ollamachat.agent.infra.observability;

import java.time.Duration;
import java.time.Instant;

public final class ExecutionTimer {

    private ExecutionTimer() {
        // util class
    }

    public static long elapsedMillis() {
        TraceContext ctx = TraceContextHolder.get();
        if (ctx == null) {
            return -1;
        }
        return Duration.between(ctx.startTime(), Instant.now()).toMillis();
    }
}

