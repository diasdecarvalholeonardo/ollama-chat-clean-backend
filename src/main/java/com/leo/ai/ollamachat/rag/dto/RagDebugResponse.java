package com.leo.ai.ollamachat.rag.dto;

import com.leo.ai.ollamachat.rag.metrics.RagMetrics;

import java.util.List;

/**
 * RagDebugResponse avançado para profiling completo do pipeline RAG.
 */
public class RagDebugResponse {

    private String question;
    private List<String> queries;

    // Contadores de chunks
    private int retrievedChunks;
    private int rerankedChunks;
    private int compressedChunks;
    private int uniqueChunks;

    // Contexto e prompt final usados para LLM
    private String context;
    private String prompt;  // Mantendo nomenclatura original

    // Resposta final da LLM
    private String answer;

    // Flags de verificação
    private boolean verified;

    // Métricas detalhadas
    private RagMetrics metrics;

    // =========================
    // CONSTRUTORES
    // =========================
    public RagDebugResponse() {}

    public RagDebugResponse(String question, List<String> queries,
                            int retrievedChunks, int rerankedChunks, int compressedChunks, int uniqueChunks,
                            String context, String prompt, String answer, boolean verified,
                            RagMetrics metrics) {
        this.question = question;
        this.queries = queries;
        this.retrievedChunks = retrievedChunks;
        this.rerankedChunks = rerankedChunks;
        this.compressedChunks = compressedChunks;
        this.uniqueChunks = uniqueChunks;
        this.context = context;
        this.prompt = prompt;
        this.answer = answer;
        this.verified = verified;
        this.metrics = metrics;
    }

    // =========================
    // GETTERS e SETTERS
    // =========================
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public List<String> getQueries() { return queries; }
    public void setQueries(List<String> queries) { this.queries = queries; }

    public int getRetrievedChunks() { return retrievedChunks; }
    public void setRetrievedChunks(int retrievedChunks) { this.retrievedChunks = retrievedChunks; }

    public int getRerankedChunks() { return rerankedChunks; }
    public void setRerankedChunks(int rerankedChunks) { this.rerankedChunks = rerankedChunks; }

    public int getCompressedChunks() { return compressedChunks; }
    public void setCompressedChunks(int compressedChunks) { this.compressedChunks = compressedChunks; }

    public int getUniqueChunks() { return uniqueChunks; }
    public void setUniqueChunks(int uniqueChunks) { this.uniqueChunks = uniqueChunks; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public RagMetrics getMetrics() { return metrics; }
    public void setMetrics(RagMetrics metrics) { this.metrics = metrics; }

    // =========================
    // HELPERS
    // =========================
    public boolean hasFastResponse() {
        return metrics != null && metrics.isFastResponse();
    }

    public double getCacheHitRate() {
        return metrics != null ? metrics.getCacheHitRate() : 0.0;
    }

    // =========================
    // DEBUG / TO STRING
    // =========================
    @Override
    public String toString() {
        return "RagDebugResponse{" +
                "question='" + question + '\'' +
                ", queries=" + queries +
                ", retrievedChunks=" + retrievedChunks +
                ", rerankedChunks=" + rerankedChunks +
                ", compressedChunks=" + compressedChunks +
                ", uniqueChunks=" + uniqueChunks +
                ", context='" + context + '\'' +
                ", prompt='" + prompt + '\'' +
                ", answer='" + answer + '\'' +
                ", verified=" + verified +
                ", metrics=" + metrics +
                '}';
    }
}