package com.leo.ai.ollamachat.rag.cache;

public interface SemanticCacheProjection {

    String getAnswer();

    Double getDistance(); // opcional
}
