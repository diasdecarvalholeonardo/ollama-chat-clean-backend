package com.leo.ai.ollamachat.embedding.cache;

public class CachedResult {

    private final String response;
    private final double score;

    public CachedResult(String response, double score) {
        this.response = response;
        this.score = score;
    }

    public String getResponse() {
        return response;
    }

    public double getScore() {
        return score;
    }
}
