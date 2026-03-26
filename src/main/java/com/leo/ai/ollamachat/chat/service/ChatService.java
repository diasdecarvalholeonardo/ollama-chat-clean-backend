package com.leo.ai.ollamachat.chat.service;

import com.leo.ai.ollamachat.chat.dto.ChatRequest;
import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.persistence.mongo.chat.ChatMessageDocument;
import com.leo.ai.ollamachat.rag.service.*;
import com.leo.ai.ollamachat.retrieval.service.HybridRetrievalService;
import com.leo.ai.ollamachat.service.chat.ChatHistoryService.ChatHistoryService;

import com.leo.ai.ollamachat.cache.response.ContextFusionService;
import com.leo.ai.ollamachat.cache.response.ResponseCacheService;
import com.leo.ai.ollamachat.embedding.cache.SemanticCacheService;
import com.leo.ai.ollamachat.embedding.service.EmbeddingService;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {

    private final QueryRewriteService rewriteService;
    private final HybridRetrievalService hybridSearchService;
    private final RerankService rerankService;
    private final ScoreFusionService scoreFusionService;
    private final ContextCompressionService compressionService;
    private final ChatHistoryService historyService;
    private final ChatModel chatModel;
    private final QueryExpansionService queryExpansionService;

    // 🔥 CACHE LAYERS
    private final ResponseCacheService responseCacheService;
    private final SemanticCacheService semanticCacheService;
    private final EmbeddingService embeddingService;
    private final ContextFusionService contextFusionService;

    public ChatService(
            QueryRewriteService rewriteService,
            HybridRetrievalService hybridSearchService,
            RerankService rerankService,
            ScoreFusionService scoreFusionService,
            ContextCompressionService compressionService,
            ChatHistoryService historyService,
            ChatModel chatModel,
            QueryExpansionService queryExpansionService,
            ResponseCacheService responseCacheService,
            EmbeddingService embeddingService,
            SemanticCacheService semanticCacheService,   // 🔥 CORRIGIDO
            ContextFusionService contextFusionService    // 🔥 CORRIGIDO
    ) {
        this.rewriteService = rewriteService;
        this.hybridSearchService = hybridSearchService;
        this.rerankService = rerankService;
        this.scoreFusionService = scoreFusionService;
        this.compressionService = compressionService;
        this.historyService = historyService;
        this.chatModel = chatModel;
        this.queryExpansionService = queryExpansionService;
        this.responseCacheService = responseCacheService;
        this.embeddingService = embeddingService;
        this.semanticCacheService = semanticCacheService;
        this.contextFusionService = contextFusionService;
    }

    public ChatMessageDocument chat(ChatRequest request) {

        String userQuery = request.getMessage();

        String memoryContext = historyService.buildContext(
                request.getSessionId(), 10
        );

        String fusedInput =
                contextFusionService.fuse(userQuery, memoryContext);

        float[] embedding =
                embeddingService.embed(fusedInput);

        // 🔥 RESPONSE CACHE
        Optional<String> responseHit =
                responseCacheService.findSimilar(embedding);

        if (responseHit.isPresent()) {
            return save(request, responseHit.get());
        }

        // 🔥 SEMANTIC CACHE (RETORNA EMBEDDING!)
        Optional<float[]> semanticHit =
                semanticCacheService.findSimilar(embedding);

        if (semanticHit.isPresent()) {

            // 🔥 BUSCA RESPOSTA USANDO EMBEDDING ENCONTRADO
            Optional<String> semanticResponse =
                    responseCacheService.findSimilar(semanticHit.get());

            if (semanticResponse.isPresent()) {
                return save(request, semanticResponse.get());
            }
        }

        // 🔁 RAG NORMAL
        String rewrittenQuery = rewriteService.rewrite(userQuery);

        String expandedQuery =
                queryExpansionService.expand(rewrittenQuery, memoryContext);

        List<DocumentChunk> documents =
                hybridSearchService.search(expandedQuery, 10);

        List<DocumentChunk> rerankedDocs =
                rerankService.rerank(rewrittenQuery, documents);

        List<DocumentChunk> fusedDocs =
                scoreFusionService.fuse(rerankedDocs, rewrittenQuery);

        String ragContext =
                compressionService.compressDocuments(
                        rewrittenQuery,
                        fusedDocs.stream()
                                .map(c -> new Document(c.getContent()))
                                .toList()
                );

        if (ragContext == null || ragContext.isBlank()) {
            ragContext = "NO_RELEVANT_CONTEXT";
        }

        String promptText = "..."; // mantém o seu

        String answer = chatModel.call(new Prompt(promptText))
                .getResult().getOutput().getContent();

        // 🔥 SAVE (ASSINATURA ANTIGA)
        responseCacheService.save(
                userQuery,
                answer,
                embedding
        );

        return save(request, answer);
    }

    private ChatMessageDocument save(ChatRequest request, String answer) {
        ChatMessageDocument doc = new ChatMessageDocument();
        doc.setSessionId(request.getSessionId());
        doc.setPrompt(request.getMessage());
        doc.setResponse(answer);
        doc.setCreatedAt(Instant.now());
        return historyService.save(doc);
    }
}