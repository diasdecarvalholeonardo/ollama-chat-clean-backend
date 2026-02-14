package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;
import com.leo.ai.ollamachat.persistence.mongo.RagDebugTraceDocument;
import com.leo.ai.ollamachat.persistence.mongo.RagDebugTraceRepository;
import com.leo.ai.ollamachat.rag.debug.RagDebugTrace;
import com.leo.ai.ollamachat.rag.model.RagRetrievedChunk;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final VectorSearchService vectorSearchService;
    private final ChatClient chatClient;
    private final RagDebugTraceRepository debugRepository;

    public RagService(VectorSearchService vectorSearchService,
                      ChatClient chatClient,
                      RagDebugTraceRepository debugRepository) {
        this.vectorSearchService = vectorSearchService;
        this.chatClient = chatClient;
        this.debugRepository = debugRepository;
    }

    // 🔒 Método consagrado — NÃO MUDA
    public RagResponse ask(String question, int topK) {
        return ask(question, topK, false);
    }

    // 🧠 Método com debug opcional
    public RagResponse ask(String question, int topK, boolean debug) {

        List<AgentKnowledgeBase> contexts =
                vectorSearchService.search(question, topK);

        String contextBlock = contexts.stream()
                .map(AgentKnowledgeBase::getContent)
                .collect(Collectors.joining("\n---\n"));

        String prompt = """
                Você é um assistente técnico.
                Responda SOMENTE com base no contexto abaixo.
                Se não souber, diga que não encontrou a informação.

                CONTEXTO:
                %s

                PERGUNTA:
                %s
                """.formatted(contextBlock, question);

        String answer = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        RagDebugTrace debugTrace = null;

        if (debug) {
            RagDebugTraceDocument doc =
                    new RagDebugTraceDocument(
                            null,
                            Instant.now(),
                            question,
                            topK,
                            "nomic-embed-text",
                            "llama3.1",
                            contexts.stream()
                                    .map(RagRetrievedChunk::from)
                                    .toList()
                    );

            RagDebugTraceDocument saved = debugRepository.save(doc);

            debugTrace = new RagDebugTrace(
                    saved.timestamp(),
                    saved.question(),
                    saved.topK(),
                    saved.retrievedChunks()
            );
        }

        return new RagResponse(answer, contexts, debugTrace);
    }
}
