package com.leo.ai.ollamachat.rag.ingestion;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.embedding.service.OllamaEmbeddingService;
import com.leo.ai.ollamachat.domain.document.DocumentChunkRepository;
import org.springframework.stereotype.Service;
import java.nio.file.Path;
import java.util.List;

@Service
public class DocumentIngestionService {

    private final DocumentLoader loader;
    private final DocumentChunker chunker;
    private final OllamaEmbeddingService embeddingService;
    private final DocumentChunkRepository repository;

    public DocumentIngestionService(
            DocumentLoader loader,
            DocumentChunker chunker,
            OllamaEmbeddingService embeddingService,
            DocumentChunkRepository repository
    ) {
        this.loader = loader;
        this.chunker = chunker;
        this.embeddingService = embeddingService;
        this.repository = repository;
    }

    public void ingest(Path file) {

        System.out.println("Ingesting: " + file);

        // 1️⃣ carregar documento
        String content = loader.load(file);

        // 2️⃣ gerar chunks
        List<String> chunks = chunker.chunk(content);

        System.out.println("Chunks: " + chunks.size());

        // 3️⃣ gerar embeddings
        for (String chunk : chunks) {

            float[] embedding =
                    embeddingService.embed(chunk);

            DocumentChunk entity = new DocumentChunk();

            entity.setContent(chunk);
            entity.setEmbedding(embedding);

            repository.save(entity);
        }

        System.out.println("Ingestion finished.");
    }
}
