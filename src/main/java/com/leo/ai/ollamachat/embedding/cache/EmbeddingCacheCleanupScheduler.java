package com.leo.ai.ollamachat.embedding.cache;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingCacheCleanupScheduler {

    private final EmbeddingCacheService cacheService;

    public EmbeddingCacheCleanupScheduler(EmbeddingCacheService cacheService) {
        this.cacheService = cacheService;
    }

    // 🧹 roda a cada 1 hora
    @Scheduled(fixedRate = 3600000)
    public void cleanup() {

        cacheService.cleanExpired();

    }
}
