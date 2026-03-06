package com.leo.ai.ollamachat.service.document;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.domain.document.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentChunkService {

    private final DocumentChunkRepository repository;

    public DocumentChunkService(DocumentChunkRepository repository) {
        this.repository = repository;
    }

    // ---------- CRUD ----------

    public DocumentChunk save(DocumentChunk chunk) {
        return repository.save(chunk);
    }

    public List<DocumentChunk> findAll() {
        return repository.findAll();
    }

    // ---------- CHUNKING ----------

    public List<DocumentChunk> chunkAndSave(String text) {

        List<DocumentChunk> chunks = new ArrayList<>();

        int chunkSize = 500;
        int index = 0;
        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            String piece = text.substring(start, end);

            DocumentChunk chunk = new DocumentChunk();
            chunk.setContent(piece);
            chunk.setChunkIndex(index);

            repository.save(chunk);

            chunks.add(chunk);

            start = end;
            index++;
        }

        return chunks;
    }
}