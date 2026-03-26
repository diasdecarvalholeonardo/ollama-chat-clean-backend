package com.leo.ai.ollamachat.rag.service;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContextCompressionService {

    private final ChatModel chatModel;

    public ContextCompressionService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    // ✅ 1. LEGADO (mantém compatibilidade total com seu domínio)
    public List<DocumentChunk> compressChunks(String question, List<DocumentChunk> chunks) {

        if (chunks == null || chunks.isEmpty()) {
            return chunks;
        }

        String compressedText = compressInternal(
                question,
                chunks.stream()
                        .map(DocumentChunk::getContent)
                        .collect(Collectors.toList())
        );

        // 🔥 fallback seguro (mantém comportamento original)
        if (compressedText.isBlank()) {
            return chunks;
        }

        List<DocumentChunk> result = new ArrayList<>();

        DocumentChunk newChunk = new DocumentChunk();
        newChunk.setContent(compressedText);

        result.add(newChunk);

        return result;
    }

    // ✅ 2. NOVO (pipeline moderno usando Document)
    public String compressDocuments(String query, List<Document> documents) {

        if (documents == null || documents.isEmpty()) {
            return "";
        }

        return compressInternal(
                query,
                documents.stream()
                        .map(Document::getContent)
                        .collect(Collectors.toList())
        );
    }

    // ✅ 3. NOVO (🔥 PADRÃO DO SEU PROJETO ATUAL)
    // 👉 Esse é o método que seu ChatService deve usar
    public String compressChunksToString(String query, List<DocumentChunk> chunks) {

        if (chunks == null || chunks.isEmpty()) {
            return "";
        }

        return compressInternal(
                query,
                chunks.stream()
                        .map(DocumentChunk::getContent)
                        .collect(Collectors.toList())
        );
    }

    // 🔥 CORE ÚNICO (evita duplicação e bugs)
    private String compressInternal(String query, List<String> texts) {

        String joinedContext = texts.stream()
                .map(this::sanitize)
                .filter(s -> !s.isBlank())
                .distinct()
                .collect(Collectors.joining("\n\n---\n\n"));

        if (joinedContext.isBlank()) {
            return "";
        }

        joinedContext = truncate(joinedContext, 6000);

        String prompt = """
                You are an expert in information extraction.

                Extract ONLY the information relevant to the query.

                RULES:
                - Keep only useful facts
                - Remove redundancy
                - Be concise
                - Do NOT explain
                - If nothing is relevant, return: NONE

                Query:
                %s

                Context:
                %s

                Relevant information:
                """.formatted(query, joinedContext);

        try {

            String response = chatModel.call(new Prompt(prompt))
                    .getResult()
                    .getOutput()
                    .getContent()
                    .trim();

            // 🔥 fallback inteligente
            if (response.equalsIgnoreCase("NONE") || response.length() < 20) {
                return "";
            }

            return response;

        } catch (Exception e) {
            // 🔥 resiliente (produção)
            return joinedContext;
        }
    }

    private String sanitize(String text) {
        return text == null ? "" :
                text.replace("\t", " ")
                    .replace("\n", " ")
                    .trim();
    }

    private String truncate(String text, int max) {
        return text.length() > max ? text.substring(0, max) : text;
    }
}