package com.leo.ai.ollamachat.search.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;

    public SearchService(VectorStore vectorStore, EmbeddingModel embeddingModel) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
    }

    public List<Document> search(String query) {

        // 🔥 Forma nova (Spring AI)
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(5)
                .build();

        return vectorStore.similaritySearch(request);
    }
}