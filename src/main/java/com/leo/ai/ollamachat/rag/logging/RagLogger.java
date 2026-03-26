package com.leo.ai.ollamachat.rag.logging;

import com.leo.ai.ollamachat.rag.metrics.RagMetrics;
import com.leo.ai.ollamachat.rag.service.RagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RagLogger {

    private final Logger log = LoggerFactory.getLogger(RagLogger.class);

    // =====================================================
    // ✅ MÉTODOS PRINCIPAIS (AGORA NÃO-STATIC)
    // =====================================================

    public void logStart(String question) {
        log.info("RAG START | question={}", question);
    }

    public void logStep(String step, long time) {
        log.info("RAG STEP | {}={}ms", step, time);
    }

    public void logMetrics(RagMetrics metrics) {
        log.info("RAG METRICS | {}", metrics.toDebugString());
    }

    public void logFinalAnswer(String answer) {
        log.info("RAG ANSWER | size={}", answer != null ? answer.length() : 0);
    }

    // =====================================================
    // 🔥 MÉTODOS DO RAG AVANÇADO
    // =====================================================

    public void cacheHitStrong(String query, double score) {
        log.info("RAG_CACHE_STRONG_HIT | query=\"{}\" score={}", query, score);
    }

    public void cacheBoostApplied(double score) {
        log.info("RAG_CACHE_BOOST_APPLIED | score={}", score);
    }

    public void llmBypass(String query) {
        log.info("RAG_LLM_BYPASS | query=\"{}\"", query);
    }

    public void warn(String message) {
        log.warn("RAG_WARN | {}", message);
    }

    public void success(RagResponse response) {
        log.info(
            "RAG_SUCCESS | answer_length={} | sources={} | total_time={}ms",
            response.getAnswer() != null ? response.getAnswer().length() : 0,
            response.getSources() != null ? response.getSources().size() : 0,
            response.getMetrics() != null ? response.getMetrics().getTotalTime() : 0
        );
    }

    public void error(Exception e) {
        log.error("RAG_ERROR", e);
    }
}