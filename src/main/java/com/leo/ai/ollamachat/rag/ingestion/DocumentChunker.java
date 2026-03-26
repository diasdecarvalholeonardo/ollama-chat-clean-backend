package com.leo.ai.ollamachat.rag.ingestion;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class DocumentChunker {

    private static final int CHUNK_SIZE = 800;
    private static final int CHUNK_OVERLAP = 150;

    /**
     * Divide um texto grande em chunks com overlap.
     * Isso preserva contexto entre partes do documento,
     * melhorando a qualidade do RAG.
     */
    public List<String> chunk(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<String> chunks = new ArrayList<>();

        int step = CHUNK_SIZE - CHUNK_OVERLAP;
        int start = 0;

        while (start < text.length()) {

            int end = Math.min(start + CHUNK_SIZE, text.length());

            String chunk = text.substring(start, end).trim();

            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }

            start += step;
        }

        return chunks;
    }
}
