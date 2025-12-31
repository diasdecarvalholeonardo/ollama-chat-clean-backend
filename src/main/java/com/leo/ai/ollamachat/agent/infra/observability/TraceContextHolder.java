package com.leo.ai.ollamachat.agent.infra.observability;

public final class TraceContextHolder {

    private static final ThreadLocal<TraceContext> CONTEXT = new ThreadLocal<>();

    private TraceContextHolder() {
        // util class
    }

    public static void set(TraceContext context) {
        CONTEXT.set(context);
    }

    public static TraceContext get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}

