package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.ingestion.entity.AgentKnowledgeBase;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;

    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<AgentKnowledgeBase> search(String question, int topK) {

        SearchRequest request = SearchRequest.query(question)
                .withTopK(topK);

        return vectorStore.similaritySearch(request)
                .stream()
                .map(doc -> (AgentKnowledgeBase) doc.getMetadata().get("entity"))
                .toList();
    }
}
