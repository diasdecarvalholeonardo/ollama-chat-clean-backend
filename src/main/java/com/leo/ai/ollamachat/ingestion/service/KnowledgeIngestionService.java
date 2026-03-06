package com.leo.ai.ollamachat.ingestion.service;

import com.leo.ai.ollamachat.embedding.EmbeddingService;
import com.leo.ai.ollamachat.ingestion.dto.IngestionRequest;
import com.leo.ai.ollamachat.knowledge.entity.KnowledgeDocument;
import com.leo.ai.ollamachat.knowledge.repository.KnowledgeDocumentRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class KnowledgeIngestionService {

    private final KnowledgeDocumentRepository repository;
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    public KnowledgeIngestionService(
            KnowledgeDocumentRepository repository,
            EmbeddingService embeddingService,
            VectorStore vectorStore
    ) {
        this.repository = repository;
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }

    public void ingest(IngestionRequest request) {

        String content = extractContent(request);

        List<String> chunks = chunk(content, 800);

        for (String chunk : chunks) {

            KnowledgeDocument entity =
                    new KnowledgeDocument(
                            chunk,
                            request.getSourceType(),
                            request.getSourceUri()
                    );

            repository.save(entity);

            float[] embedding =
                    embeddingService.generateEmbedding(chunk);

            Map<String, Object> metadata = new HashMap<>();

            metadata.put("documentId", entity.getId());
            metadata.put("sourceType", request.getSourceType());
            metadata.put("sourceUri", request.getSourceUri());

            Document vectorDocument =
                    new Document(chunk, metadata);

            vectorStore.add(List.of(vectorDocument));
        }
    }

    private String extractContent(IngestionRequest request) {

        if (request.isText()) {

            Object text =
                    request.getMetadata().get("text");

            if (text == null) {
                throw new RuntimeException("Metadata.text is required");
            }

            return text.toString();
        }

        if (request.isPdf()) {

            try {

                File file =
                        new File(request.getSourceUri());

                PDDocument pdf =
                        PDDocument.load(file);

                PDFTextStripper stripper =
                        new PDFTextStripper();

                String text =
                        stripper.getText(pdf);

                pdf.close();

                return text;

            } catch (Exception e) {

                throw new RuntimeException(
                        "Failed to read PDF",
                        e
                );
            }
        }

        throw new RuntimeException(
                "Unsupported source type: "
                        + request.getSourceType()
        );
    }

    private List<String> chunk(String text, int chunkSize) {

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()) {

            int end =
                    Math.min(
                            start + chunkSize,
                            text.length()
                    );

            chunks.add(
                    text.substring(start, end)
            );

            start = end;
        }

        return chunks;
    }

}