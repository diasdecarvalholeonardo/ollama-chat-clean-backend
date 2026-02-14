package com.leo.ai.ollamachat.ingestion.service;

import com.leo.ai.ollamachat.ingestion.dto.IngestionRequest;
import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;
import com.leo.ai.ollamachat.ingestion.repository.AgentKnowledgeBaseRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service
@Profile("prod")
public class KnowledgeIngestionServiceImpl
        implements KnowledgeIngestionService {

    private final PdfTextExtractor pdfTextExtractor;
    private final TextChunker textChunker;
    private final EmbeddingModel embeddingModel;
    private final AgentKnowledgeBaseRepository repository;

    public KnowledgeIngestionServiceImpl(
            PdfTextExtractor pdfTextExtractor,
            TextChunker textChunker,
            EmbeddingModel embeddingModel,
            AgentKnowledgeBaseRepository repository
    ) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.textChunker = textChunker;
        this.embeddingModel = embeddingModel;
        this.repository = repository;
    }

    @Override
    @Transactional
    public void ingest(IngestionRequest request) {

        if (request.isPdf()) {

            File pdf = new File(request.getSourceUri());

            String fullText = pdfTextExtractor.extractText(pdf);

            List<String> chunks = textChunker.chunk(fullText);

            for (int i = 0; i < chunks.size(); i++) {

                String chunk = chunks.get(i);

                // 1️⃣ Gerar embedding (384 dims)
                float[] embedding = embeddingModel.embed(chunk);

                // 2️⃣ Criar entidade
                AgentKnowledgeBase entity = new AgentKnowledgeBase();
                entity.setContent(chunk);

                entity.setMetadata(
                        """
                        {
                          "source": "%s",
                          "type": "pdf",
                          "chunk_index": %d
                        }
                        """.formatted(request.getSourceUri(), i)
                );

                entity.setEmbedding(embedding);

                // 3️⃣ Persistir
                repository.save(entity);
            }
        }
    }
}
