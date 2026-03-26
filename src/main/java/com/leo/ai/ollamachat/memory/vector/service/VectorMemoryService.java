package com.leo.ai.ollamachat.memory.vector.service;

import com.leo.ai.ollamachat.embedding.service.EmbeddingService;
import com.leo.ai.ollamachat.memory.vector.model.VectorMemory;
import com.leo.ai.ollamachat.memory.vector.repository.VectorMemoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VectorMemoryService {

    private final VectorMemoryRepository repository;
    private final EmbeddingService embeddingService;

    public VectorMemoryService(
            VectorMemoryRepository repository,
            EmbeddingService embeddingService) {

        this.repository = repository;
        this.embeddingService = embeddingService;
    }

    /**
     * Busca memórias semanticamente similares
     */
    public String retrieveRelevantMemory(String query) {

        float[] embedding = embeddingService.embed(query);

        String vectorString = convertToVectorString(embedding);

        List<VectorMemory> memories =
                repository.searchSimilar(vectorString, 3);

        return memories.stream()
                .map(VectorMemory::getContent)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Salva memória no banco vetorial
     */
    public void storeMemory(String question, String answer) {

        String content = "Q: " + question + "\nA: " + answer;

        float[] embedding = embeddingService.embed(content);

        VectorMemory memory = new VectorMemory();

        memory.setContent(content);
        memory.setEmbedding(embedding); // correto: float[]

        repository.save(memory);
    }

    /**
     * Conversão local para formato pgvector
     */
    private String convertToVectorString(float[] vector) {

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {

            sb.append(vector[i]);

            if (i < vector.length - 1) {
                sb.append(",");
            }
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * Método já existente
     */
    public List<VectorMemory> searchSimilar(String embedding, int limit) {

        return repository.searchSimilar(embedding, limit);

    }
}