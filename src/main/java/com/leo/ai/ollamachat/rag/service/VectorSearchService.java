package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.leo.ai.ollamachat.chat.dto.ChatRequest;

import java.util.List;

@Service
public class VectorSearchService {

    private final VectorStore vectorStore;

    public VectorSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> search(String request2, int topK) {

        SearchRequest request =
                SearchRequest.builder()
                        .query(request2)
                        .topK(topK)
                        .build();

        return vectorStore.similaritySearch(request);
    }

	public List<Document> search(ChatRequest request, int topK) {
		// TODO Auto-generated method stub
		return null;
	}
}