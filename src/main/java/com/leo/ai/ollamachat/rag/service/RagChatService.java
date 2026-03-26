package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.chat.dto.ChatRequest;
import com.leo.ai.ollamachat.chat.dto.ChatResponse;
import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagChatService {

    private final VectorSearchService vectorSearchService;
    private final ChatClient chatClient;

    public RagChatService(
            VectorSearchService vectorSearchService,
            ChatClient.Builder chatClientBuilder
    ) {
        this.vectorSearchService = vectorSearchService;
        this.chatClient = chatClientBuilder.build();
    }

    public ChatResponse chat(ChatRequest request) {

        // 🔎 1️⃣ buscar chunks relevantes
        List<DocumentChunk> chunks =
                vectorSearchService.search(request.getMessage(), 5);

        // 📚 2️⃣ montar contexto para o LLM
        String context =
                chunks.stream()
                        .map(DocumentChunk::getContent)
                        .collect(Collectors.joining("\n\n"));

        // 🧠 3️⃣ montar prompt RAG
        String prompt =
                """
                Use the context below to answer the question.

                Context:
                %s

                Question:
                %s
                """.formatted(context, request.getMessage());

        // 🤖 4️⃣ chamar o LLM
        String answer =
                chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();

        // 📄 5️⃣ extrair fontes
        List<String> sources =
                chunks.stream()
                        .map(chunk -> "chunk_id=" + chunk.getId())
                        .collect(Collectors.toList());

        // 📦 6️⃣ resposta final
        return new ChatResponse(answer, sources);
    }
}