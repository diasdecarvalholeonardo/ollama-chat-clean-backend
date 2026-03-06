package com.leo.ai.ollamachat.embedding;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmbeddingTestController {

    private final EmbeddingService embeddingService;

    public EmbeddingTestController(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping("/api/test-embedding")
    public String test() {

        float[] vector = embeddingService.generateEmbedding(
                "Artificial intelligence is transforming software engineering"
        );

        return "Vector size: " + vector.length;
    }
}
