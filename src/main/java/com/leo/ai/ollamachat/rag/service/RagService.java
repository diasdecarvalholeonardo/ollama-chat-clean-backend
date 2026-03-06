package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.domain.document.DocumentChunkRepository;
import com.leo.ai.ollamachat.embedding.EmbeddingService;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository repository;
    private final ChatClient chatClient;

    public RagService(
            EmbeddingService embeddingService,
            DocumentChunkRepository repository,
            ChatClient.Builder chatClientBuilder
    ) {
        this.embeddingService = embeddingService;
        this.repository = repository;
        this.chatClient = chatClientBuilder.build();
    }

    public RagResponse ask(String question, int topK, boolean debug) {

        // 1️⃣ Gerar embedding da pergunta
        float[] queryEmbedding = embeddingService.generateEmbedding(question);

        // 2️⃣ Buscar chunks similares via pgvector
        List<DocumentChunk> chunks =
                repository.findTopSimilar(queryEmbedding, topK);

        // 3️⃣ Converter para Document (Spring AI)
        List<Document> documents =
                chunks.stream()
                        .map(chunk -> new Document(chunk.getContent()))
                        .collect(Collectors.toList());

        // 4️⃣ Montar contexto
        String context =
                documents.stream()
                        .map(Document::getContent)
                        .collect(Collectors.joining("\n\n"));

        // 5️⃣ Montar prompt
        String prompt =
                """
                Use the context below to answer the question.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        // 6️⃣ Chamar LLM
        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // 7️⃣ Debug
        if (debug) {
            System.out.println("\n===== DEBUG RAG =====");
            System.out.println("Question: " + question);
            System.out.println("TopK: " + topK);
            System.out.println("Chunks found: " + chunks.size());
            System.out.println("=====================\n");
        }

        return new RagResponse(answer, documents);
    }
}