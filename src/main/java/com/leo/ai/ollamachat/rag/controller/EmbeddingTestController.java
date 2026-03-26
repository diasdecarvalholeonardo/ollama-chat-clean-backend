package com.leo.ai.ollamachat.rag.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class EmbeddingTestController {

    private final EmbeddingModel embeddingModel;

    public EmbeddingTestController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/embedding")
    public float[] embed(@RequestParam String text) {
        return embeddingModel.embed(text);
    }
}

