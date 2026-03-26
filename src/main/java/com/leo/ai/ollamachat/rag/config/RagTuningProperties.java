package com.leo.ai.ollamachat.rag.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rag")
public class RagTuningProperties {

    private Fusion fusion = new Fusion();
    private Cache cache = new Cache();
    private Ranking ranking = new Ranking();

    public Fusion getFusion() { return fusion; }
    public Cache getCache() { return cache; }
    public Ranking getRanking() { return ranking; }

    public static class Fusion {
        private double cacheWeight = 1.2;
        private double retrievalWeight = 1.0;
        private double rerankWeight = 1.0;

        public double getCacheWeight() { return cacheWeight; }
        public void setCacheWeight(double cacheWeight) { this.cacheWeight = cacheWeight; }

        public double getRetrievalWeight() { return retrievalWeight; }
        public void setRetrievalWeight(double retrievalWeight) { this.retrievalWeight = retrievalWeight; }

        public double getRerankWeight() { return rerankWeight; }
        public void setRerankWeight(double rerankWeight) { this.rerankWeight = rerankWeight; }
    }

    public static class Cache {
        private double strongThreshold = 0.92;
        private double weakThreshold = 0.80;
        private int decayHours = 24;

        public double getStrongThreshold() { return strongThreshold; }
        public void setStrongThreshold(double strongThreshold) { this.strongThreshold = strongThreshold; }

        public double getWeakThreshold() { return weakThreshold; }
        public void setWeakThreshold(double weakThreshold) { this.weakThreshold = weakThreshold; }

        public int getDecayHours() { return decayHours; }
        public void setDecayHours(int decayHours) { this.decayHours = decayHours; }
    }

    public static class Ranking {
        private double minScore = 0.15;

        public double getMinScore() { return minScore; }
        public void setMinScore(double minScore) { this.minScore = minScore; }
    }
}