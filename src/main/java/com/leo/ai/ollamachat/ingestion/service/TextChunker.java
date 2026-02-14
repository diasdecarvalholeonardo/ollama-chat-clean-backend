package com.leo.ai.ollamachat.ingestion.service;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextChunker {

    private static final int CHUNK_SIZE = 800;
    private static final int OVERLAP = 100;

    public List<String> chunk(String text) {

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + CHUNK_SIZE, text.length());
            chunks.add(text.substring(start, end));
            start = end - OVERLAP;
            if (start < 0) start = 0;
        }

        return chunks;
    }
}

