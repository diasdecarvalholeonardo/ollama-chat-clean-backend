package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.agent.AgentPlannerService;
import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.embedding.cache.CachedResult;
import com.leo.ai.ollamachat.embedding.cache.SemanticCacheService;
import com.leo.ai.ollamachat.llm.LLMClient;
import com.leo.ai.ollamachat.rag.config.RagTuningProperties;
import com.leo.ai.ollamachat.rag.dto.RagDebugResponse;
import com.leo.ai.ollamachat.rag.logging.RagLogger;
import com.leo.ai.ollamachat.rag.metrics.RagMetrics;
import com.leo.ai.ollamachat.rag.router.QueryRouterService;
import com.leo.ai.ollamachat.rag.router.QueryType;
import com.leo.ai.ollamachat.retrieval.service.HybridRetrievalService;
import com.leo.ai.ollamachat.retrieval.service.WebSearchService;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final HybridRetrievalService hybridSearchService;
    private final RerankerService rerankerService;
    private final MultiQueryService multiQueryService;
    private final ContextCompressionService contextCompressionService;
    private final SelfRagService selfRagService;
    private final QueryRouterService queryRouterService;
    private final WebSearchService webSearchService;
    private final VectorSearchService vectorSearchService;
    private final AgentPlannerService agentPlannerService;
    private final SemanticCacheService cacheService;
    private final RagLogger ragLogger;

    private final LLMClient llm;
    private final RagTuningProperties tuning;

    private static final double CACHE_STRONG_THRESHOLD = 0.92;
    private static final double CACHE_WEAK_THRESHOLD = 0.80;
    private static final int MAX_CONTEXT_CHARS = 12000;

    @Value("${rag.retrieval.top-k:5}")
    private int defaultTopK;

    public RagService(
            HybridRetrievalService hybridSearchService,
            RerankerService rerankerService,
            MultiQueryService multiQueryService,
            ContextCompressionService contextCompressionService,
            SelfRagService selfRagService,
            QueryRouterService queryRouterService,
            VectorSearchService vectorSearchService,
            WebSearchService webSearchService,
            AgentPlannerService agentPlannerService,
            SemanticCacheService cacheService,
            LLMClient llm,
            RagTuningProperties tuning,
            RagLogger ragLogger
    ) {
        this.hybridSearchService = hybridSearchService;
        this.rerankerService = rerankerService;
        this.multiQueryService = multiQueryService;
        this.contextCompressionService = contextCompressionService;
        this.selfRagService = selfRagService;
        this.queryRouterService = queryRouterService;
        this.vectorSearchService = vectorSearchService;
        this.webSearchService = webSearchService;
        this.agentPlannerService = agentPlannerService;
        this.cacheService = cacheService;
        this.llm = llm;
        this.tuning = tuning;
        this.ragLogger = ragLogger;
    }

    public RagResponse ask(String question, Integer topK, boolean debug) {

        long start = System.currentTimeMillis();
        RagMetrics metrics = new RagMetrics();

        ragLogger.logStart(question);

        try {
            // =====================================================
            // 🔥 CACHE (READ)
            // =====================================================
            Optional<CachedResult> cacheResult = cacheService.findBestMatch(question);

            double cacheScore = cacheResult.map(CachedResult::getScore).orElse(0.0);

            if (cacheScore >= CACHE_STRONG_THRESHOLD && cacheResult.isPresent()) {

                ragLogger.cacheHitStrong(question, cacheScore);

                metrics.setTotalTime(System.currentTimeMillis() - start);

                return new RagResponse(
                        cacheResult.get().getResponse(),
                        List.of(),
                        metrics
                );
            }

            boolean useCacheBoost = cacheScore >= CACHE_WEAK_THRESHOLD;

            // =====================================================
            // 🔥 ROUTER
            // =====================================================
            QueryType route = queryRouterService.route(question);
            metrics.setRoute(route.name());

            List<String> plannedQueries = agentPlannerService.plan(question);

            if (route == QueryType.LLM) {

                long t = System.currentTimeMillis();
                String answer = llm.generate(question);
                metrics.setGenerationTime(ms(t));

                cacheService.store(question, answer);

                ragLogger.llmBypass(question);

                metrics.setTotalTime(System.currentTimeMillis() - start);

                return new RagResponse(answer, List.of(), metrics);
            }

            int effectiveTopK = (topK == null || topK <= 0) ? defaultTopK : topK;
            int searchK = Math.max(effectiveTopK * 4, 20);

            // =====================================================
            // 1️⃣ MULTI QUERY
            // =====================================================
            long tMulti = System.currentTimeMillis();

            List<String> queries = new ArrayList<>();
            queries.add(question);

            try {
                queries.addAll(multiQueryService.generateQueries(question));
            } catch (Exception e) {
                ragLogger.warn("MultiQuery failed");
            }

            metrics.setQueriesGenerated(queries.size());
            metrics.setExpansionTime(ms(tMulti));

            // =====================================================
            // 2️⃣ RETRIEVAL
            // =====================================================
            long tRetrieval = System.currentTimeMillis();

            List<DocumentChunk> retrieved = new ArrayList<>();

            for (String q : queries) {
                List<DocumentChunk> results = hybridSearchService.search(q, searchK);

                if (results == null || results.isEmpty()) {
                    results = vectorSearchService.search(q, searchK);
                }

                retrieved.addAll(results);
            }

            metrics.setRetrievedChunks(retrieved.size());
            metrics.setRetrievalTime(ms(tRetrieval));

            // =====================================================
            // 3️⃣ DEDUP
            // =====================================================
            List<DocumentChunk> uniqueChunks = deduplicateChunks(retrieved);
            metrics.setUniqueChunks(uniqueChunks.size());

            // =====================================================
            // 4️⃣ RERANK
            // =====================================================
            long tRerank = System.currentTimeMillis();

            List<DocumentChunk> reranked =
                    rerankerService.rerank(question, uniqueChunks, effectiveTopK);

            metrics.setRerankedChunks(reranked.size());
            metrics.setRerankTime(ms(tRerank));

            normalizeScores(reranked);

            // =====================================================
            // 🔥 CACHE FUSION
            // =====================================================
            if (useCacheBoost && cacheResult.isPresent()) {

                DocumentChunk cacheChunk = buildCacheChunk(cacheResult.get());
                cacheChunk.setScore(cacheChunk.getScore() * 1.2);

                reranked.add(0, cacheChunk);

                ragLogger.cacheBoostApplied(cacheScore);
            }

            reranked.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

            // =====================================================
            // 5️⃣ COMPRESSION
            // =====================================================
            long tCompression = System.currentTimeMillis();

            List<DocumentChunk> compressedChunks =
                    contextCompressionService.compressChunks(question, reranked);

            if (compressedChunks.isEmpty()) {
                compressedChunks = reranked;
            }

            metrics.setCompressedChunks(compressedChunks.size());
            metrics.setCompressionTime(ms(tCompression));

            // =====================================================
            // 6️⃣ CONTEXT
            // =====================================================
            String context = trimContext(buildContextWithSources(compressedChunks));

            // =====================================================
            // 7️⃣ LLM
            // =====================================================
            long tLLM = System.currentTimeMillis();

            String answer = llm.generate(question, buildSystemPrompt(context));

            metrics.setGenerationTime(ms(tLLM));

            // =====================================================
            // 8️⃣ SELF-RAG
            // =====================================================
            long tVerify = System.currentTimeMillis();

            boolean verified = selfRagService.verify(question, answer, context);

            metrics.setVerificationTime(ms(tVerify));
            metrics.setVerified(verified);

            if (!verified) {
                answer = "I could not verify this answer against the knowledge base.";
            }

            // =====================================================
            // RESULT
            // =====================================================
            List<Document> documents = compressedChunks.stream()
                    .map(c -> new Document(c.getContent()))
                    .toList();

            metrics.setTotalTime(System.currentTimeMillis() - start);

            cacheService.store(question, answer);

            RagResponse response = new RagResponse(answer, documents, metrics);

            ragLogger.success(response);

            return response;

        } catch (Exception e) {

            metrics.setTotalTime(System.currentTimeMillis() - start);

            ragLogger.error(e);

            throw new RuntimeException("Erro no pipeline RAG", e);
        }
    }

    private long ms(long start) {
        return System.currentTimeMillis() - start;
    }

    // ================= AUX =================

    private void normalizeScores(List<DocumentChunk> chunks) {
        if (chunks == null || chunks.isEmpty()) return;

        double max = chunks.stream()
                .mapToDouble(DocumentChunk::getScore)
                .max()
                .orElse(1.0);

        if (max == 0) max = 1.0;

        for (DocumentChunk c : chunks) {
            c.setScore(c.getScore() / max);
        }
    }

    private DocumentChunk buildCacheChunk(CachedResult cacheResult) {
        DocumentChunk chunk = new DocumentChunk();
        chunk.setContent(cacheResult.getResponse());
        chunk.setScore(cacheResult.getScore());
        chunk.setSource("CACHE");
        return chunk;
    }

    private List<DocumentChunk> deduplicateChunks(List<DocumentChunk> chunks) {
        Set<String> seen = new HashSet<>();
        return chunks.stream()
                .filter(chunk -> seen.add(chunk.getContent()))
                .collect(Collectors.toList());
    }

    private String buildContextWithSources(List<DocumentChunk> chunks) {
        StringBuilder context = new StringBuilder();

        for (int i = 0; i < chunks.size(); i++) {
            context.append("[")
                    .append(i + 1)
                    .append("] ")
                    .append(chunks.get(i).getContent())
                    .append("\n\n");
        }

        return context.toString();
    }

    private String trimContext(String context) {
        return context.length() <= MAX_CONTEXT_CHARS
                ? context
                : context.substring(0, MAX_CONTEXT_CHARS);
    }

    private String buildSystemPrompt(String context) {
        return """
        You are an AI assistant.

        Answer ONLY using the provided context.

        Rules:
        - Do not hallucinate
        - If not found, say you don't know
        - Cite sources [1], [2]

        Context:
        """ + context;
    }
    
    public RagDebugResponse debug(String question) {

        long start = System.currentTimeMillis();
        RagMetrics metrics = new RagMetrics();

        try {
            ragLogger.logStart(question);

            int topK = defaultTopK;
            int searchK = Math.max(topK * 4, 20);

            // =====================================================
            // 1️⃣ MULTI QUERY (CORRIGIDO)
            // =====================================================
            long tMulti = System.currentTimeMillis();

            List<String> queries = new ArrayList<>();
            queries.add(question);

            try {
                queries.addAll(multiQueryService.generateQueries(question));
            } catch (Exception e) {
                ragLogger.warn("MultiQuery failed");
            }

            metrics.setQueriesGenerated(queries.size());
            metrics.setExpansionTime(ms(tMulti));

            // =====================================================
            // 2️⃣ RETRIEVAL (CORRIGIDO)
            // =====================================================
            long tRetrieval = System.currentTimeMillis();

            List<DocumentChunk> retrieved = new ArrayList<>();

            for (String q : queries) {
                List<DocumentChunk> results = hybridSearchService.search(q, searchK);

                if (results == null || results.isEmpty()) {
                    results = vectorSearchService.search(q, searchK);
                }

                retrieved.addAll(results);
            }

            metrics.setRetrievedChunks(retrieved.size());
            metrics.setRetrievalTime(ms(tRetrieval));

            // =====================================================
            // 3️⃣ DEDUP
            // =====================================================
            List<DocumentChunk> uniqueChunks = deduplicateChunks(retrieved);
            metrics.setUniqueChunks(uniqueChunks.size());

            // =====================================================
            // 4️⃣ RERANK (CORRIGIDO)
            // =====================================================
            long tRerank = System.currentTimeMillis();

            List<DocumentChunk> reranked =
                    rerankerService.rerank(question, uniqueChunks, topK);

            metrics.setRerankedChunks(reranked.size());
            metrics.setRerankTime(ms(tRerank));

            // =====================================================
            // 5️⃣ CONTEXT
            // =====================================================
            String context = trimContext(buildContextWithSources(reranked));

            // =====================================================
            // 6️⃣ PROMPT (CORRIGIDO)
            // =====================================================
            String prompt = buildSystemPrompt(context);

            // =====================================================
            // 7️⃣ LLM (CORRIGIDO)
            // =====================================================
            long tLLM = System.currentTimeMillis();

            String answer = llm.generate(question, prompt);

            metrics.setGenerationTime(ms(tLLM));
            metrics.setTotalTime(System.currentTimeMillis() - start);

            ragLogger.logMetrics(metrics);
            ragLogger.logFinalAnswer(answer);

            // =====================================================
            // 8️⃣ DEBUG RESPONSE (SIMPLIFICADO E COMPATÍVEL)
            // =====================================================
            RagDebugResponse debug = new RagDebugResponse();
            debug.setQuestion(question);
            debug.setAnswer(answer);
            debug.setMetrics(metrics);

            return debug;

        } catch (Exception e) {
            ragLogger.error(e);
            throw new RuntimeException("Erro no debug RAG", e);
        }
    }
    
}