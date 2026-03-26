package com.leo.ai.ollamachat.rag.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * RagMetrics - versão FINAL (fusionada + production-ready)
 */
public class RagMetrics {

    // =========================
    // CONTADORES
    // =========================
    private int queriesGenerated;
    private int retrievedChunks;
    private int uniqueChunks;
    private int rerankedChunks;
    private int compressedChunks;

    private int cacheHits;
    private int cacheMisses;
    private int fusionCandidates;

    // =========================
    // TEMPOS (ms)
    // =========================
    private long expansionTime;     // 🔥 NOVO
    private long retrievalTime;
    private long rerankTime;
    private long compressionTime;
    private long generationTime;    // 🔥 NOVO
    private long verificationTime;  // 🔥 NOVO
    private long totalTime;

    private long cacheTime;
    private long fusionTime;
    private long llmTime;

    // =========================
    // CONTROLE
    // =========================
    private boolean verified;
    private String route; // 🔥 NOVO

    // =========================
    // TIMERS INTERNOS
    // =========================
    private long startTime;
    private long stepStart;

    private Map<String, Long> stepTimes = new HashMap<>();

    // =========================
    // CONSTRUTORES
    // =========================
    public RagMetrics() {}

    // =========================
    // GETTERS / SETTERS
    // =========================

    public int getQueriesGenerated() { return queriesGenerated; }
    public void setQueriesGenerated(int queriesGenerated) { this.queriesGenerated = queriesGenerated; }

    public int getRetrievedChunks() { return retrievedChunks; }
    public void setRetrievedChunks(int retrievedChunks) { this.retrievedChunks = retrievedChunks; }

    public int getUniqueChunks() { return uniqueChunks; }
    public void setUniqueChunks(int uniqueChunks) { this.uniqueChunks = uniqueChunks; }

    public int getRerankedChunks() { return rerankedChunks; }
    public void setRerankedChunks(int rerankedChunks) { this.rerankedChunks = rerankedChunks; }

    public int getCompressedChunks() { return compressedChunks; }
    public void setCompressedChunks(int compressedChunks) { this.compressedChunks = compressedChunks; }

    public int getCacheHits() { return cacheHits; }
    public void setCacheHits(int cacheHits) { this.cacheHits = cacheHits; }

    public int getCacheMisses() { return cacheMisses; }
    public void setCacheMisses(int cacheMisses) { this.cacheMisses = cacheMisses; }

    public int getFusionCandidates() { return fusionCandidates; }
    public void setFusionCandidates(int fusionCandidates) { this.fusionCandidates = fusionCandidates; }

    public long getExpansionTime() { return expansionTime; }
    public void setExpansionTime(long expansionTime) { this.expansionTime = expansionTime; }

    public long getRetrievalTime() { return retrievalTime; }
    public void setRetrievalTime(long retrievalTime) { this.retrievalTime = retrievalTime; }

    public long getRerankTime() { return rerankTime; }
    public void setRerankTime(long rerankTime) { this.rerankTime = rerankTime; }

    public long getCompressionTime() { return compressionTime; }
    public void setCompressionTime(long compressionTime) { this.compressionTime = compressionTime; }

    public long getGenerationTime() { return generationTime; }
    public void setGenerationTime(long generationTime) { this.generationTime = generationTime; }

    public long getVerificationTime() { return verificationTime; }
    public void setVerificationTime(long verificationTime) { this.verificationTime = verificationTime; }

    public long getTotalTime() { return totalTime; }
    public void setTotalTime(long totalTime) { this.totalTime = totalTime; }

    public long getCacheTime() { return cacheTime; }
    public void setCacheTime(long cacheTime) { this.cacheTime = cacheTime; }

    public long getFusionTime() { return fusionTime; }
    public void setFusionTime(long fusionTime) { this.fusionTime = fusionTime; }

    public long getLlmTime() { return llmTime; }
    public void setLlmTime(long llmTime) { this.llmTime = llmTime; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public Map<String, Long> getStepTimes() { return stepTimes; }
    public void setStepTimes(Map<String, Long> stepTimes) { this.stepTimes = stepTimes; }

    // =========================
    // HELPERS
    // =========================
    public void incrementCacheHit() { this.cacheHits++; }
    public void incrementCacheMiss() { this.cacheMisses++; }

    public double getCacheHitRate() {
        int total = cacheHits + cacheMisses;
        return total == 0 ? 0.0 : (double) cacheHits / total;
    }

    public boolean isFastResponse() {
        return totalTime < 1500;
    }

    public boolean isGoodRetrieval() {
        return retrievedChunks > 0 && uniqueChunks > 0;
    }

    public boolean isGoodCompression() {
        return compressedChunks > 0 && compressedChunks <= rerankedChunks;
    }

    // =========================
    // TIMERS
    // =========================
    public void startTotalTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public void stopTotalTimer() {
        this.totalTime = System.currentTimeMillis() - startTime;
    }

    public void startStep() {
        this.stepStart = System.currentTimeMillis();
    }

    public long stopStep() {
        return System.currentTimeMillis() - stepStart;
    }

    public void recordStep(String stepName, long time) {
        stepTimes.put(stepName, time);
    }

    public long getStepTime(String stepName) {
        return stepTimes.getOrDefault(stepName, 0L);
    }

    // =========================
    // DEBUG / LOGGING
    // =========================
    public String toDebugString() {
        return "RAG METRICS DEBUG -> " +
                "queries=" + queriesGenerated +
                ", retrieved=" + retrievedChunks +
                ", unique=" + uniqueChunks +
                ", reranked=" + rerankedChunks +
                ", compressed=" + compressedChunks +
                ", fusionCandidates=" + fusionCandidates +
                ", cacheHitRate=" + getCacheHitRate() +
                ", route=" + route +
                ", totalTime=" + totalTime + "ms" +
                ", steps=" + stepTimes;
    }

    @Override
    public String toString() {
        return "RagMetrics{" +
                "queries=" + queriesGenerated +
                ", retrieved=" + retrievedChunks +
                ", unique=" + uniqueChunks +
                ", reranked=" + rerankedChunks +
                ", compressed=" + compressedChunks +
                ", cacheHits=" + cacheHits +
                ", cacheMisses=" + cacheMisses +
                ", hitRate=" + getCacheHitRate() +
                ", totalTime=" + totalTime + "ms" +
                ", llmTime=" + llmTime + "ms" +
                ", route=" + route +
                '}';
    }
}