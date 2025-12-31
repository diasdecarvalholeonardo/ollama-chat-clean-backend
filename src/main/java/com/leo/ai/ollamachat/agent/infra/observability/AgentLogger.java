package com.leo.ai.ollamachat.agent.infra.observability;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AgentLogger {

    private static final Logger log =
            LoggerFactory.getLogger("AGENT");

    public void logPrompt(String prompt) {
        TraceContext ctx = TraceContextHolder.get();
        if (ctx == null) return;

        log.info(
            "traceId={} event=prompt content={}",
            ctx.traceId(),
            prompt
        );
    }

    public void logResponse(String response) {
        TraceContext ctx = TraceContextHolder.get();
        if (ctx == null) return;

        log.info(
            "traceId={} event=response content={}",
            ctx.traceId(),
            response
        );
    }

    public void logLatency() {
        TraceContext ctx = TraceContextHolder.get();
        if (ctx == null) return;

        long latencyMs = ExecutionTimer.elapsedMillis();

        log.info(
            "traceId={} event=latency latencyMs={}",
            ctx.traceId(),
            latencyMs
        );
    }
}

