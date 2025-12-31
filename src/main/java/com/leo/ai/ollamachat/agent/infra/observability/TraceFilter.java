package com.leo.ai.ollamachat.agent.infra.observability;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
public class TraceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String traceId = UUID.randomUUID().toString();

        TraceContext context = new TraceContext(
                traceId,
                Instant.now()
        );

        TraceContextHolder.set(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TraceContextHolder.clear();
        }
    }
}

