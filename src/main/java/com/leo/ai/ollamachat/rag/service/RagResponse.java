package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.rag.metrics.RagMetrics;
import org.springframework.ai.document.Document;

import java.util.List;

/**
 * Resposta final do pipeline RAG
 * Compatível com:
 * - endpoints REST
 * - debug
 * - observabilidade
 */
public class RagResponse {

    // =========================
    // CORE
    // =========================
    private String answer;

    // 🔥 Mantido (seu código)
    private List<Document> sources;

    // 🔥 Métricas avançadas
    private RagMetrics metrics;

    // =========================
    // CONSTRUTORES
    // =========================

    // Necessário para Jackson
    public RagResponse() {}

    // 🔥 Construtor completo (compatível com seu código atual)
    public RagResponse(String answer, List<Document> sources, RagMetrics metrics) {
        this.answer = answer;
        this.sources = sources;
        this.metrics = metrics;
    }

    // 🔥 Construtor simplificado (minha proposta, útil em fallback/casos rápidos)
    public RagResponse(String answer, RagMetrics metrics) {
        this.answer = answer;
        this.metrics = metrics;
    }

    // =========================
    // GETTERS
    // =========================
    public String getAnswer() {
        return answer;
    }

    public List<Document> getSources() {
        return sources;
    }

    public RagMetrics getMetrics() {
        return metrics;
    }

    // =========================
    // SETTERS
    // =========================
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setSources(List<Document> sources) {
        this.sources = sources;
    }

    public void setMetrics(RagMetrics metrics) {
        this.metrics = metrics;
    }

    // =========================
    // HELPERS (🔥 PRODUÇÃO)
    // =========================

    /**
     * Verifica se há fontes no contexto
     */
    public boolean hasSources() {
        return sources != null && !sources.isEmpty();
    }

    /**
     * Quantidade de fontes usadas
     */
    public int getSourceCount() {
        return sources != null ? sources.size() : 0;
    }

    /**
     * Verifica se há métricas
     */
    public boolean hasMetrics() {
        return metrics != null;
    }

    /**
     * 🔥 Novo: resposta veio do contexto RAG ou fallback?
     */
    public boolean isFromRag() {
        return hasSources();
    }

    /**
     * 🔥 Novo: resposta rápida (baseado em métricas)
     */
    public boolean isFast() {
        return metrics != null && metrics.isFastResponse();
    }

    /**
     * 🔥 Novo: resposta confiável (heurística simples)
     */
    public boolean isReliable() {
        return hasSources() &&
               metrics != null &&
               metrics.isGoodRetrieval();
    }

    // =========================
    // DEBUG / LOG
    // =========================
    @Override
    public String toString() {
        return "RagResponse{" +
                "answerLength=" + (answer != null ? answer.length() : 0) +
                ", sourcesCount=" + getSourceCount() +
                ", hasMetrics=" + hasMetrics() +
                ", metrics=" + metrics +
                '}';
    }
}