package com.leo.ai.ollamachat.service;

import com.leo.ai.ollamachat.document.ChatMessageDocument;
import com.leo.ai.ollamachat.dto.ChatMessageRequest;
import com.leo.ai.ollamachat.repository.mongo.ChatMessageMongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class ChatHistoryService {

    private final ChatMessageMongoRepository repository;

    public ChatHistoryService(ChatMessageMongoRepository repository) {
        this.repository = repository;
    }

    public ChatMessageDocument save(ChatMessageRequest request) {
        ChatMessageDocument doc = new ChatMessageDocument();
        doc.setSessionId(request.getSessionId());
        doc.setPrompt(request.getPrompt());
        doc.setResponse(request.getResponse());
        doc.setCreatedAt(Instant.now());
        return repository.save(doc);
    }

    public ChatMessageDocument save(ChatMessageDocument doc) {
        return repository.save(doc);
    }

    public Page<ChatMessageDocument> findBySession(
            String sessionId,
            int page,
            int size
    ) {
        return repository.findBySessionIdOrderByCreatedAtDesc(
                sessionId,
                PageRequest.of(page, size)
        );
    }

    public String buildContext(String sessionId, int limit) {
        var page = repository.findBySessionIdOrderByCreatedAtDesc(
                sessionId,
                PageRequest.of(0, limit)
        );

        StringBuilder context = new StringBuilder();

        page.getContent()
            .reversed()
            .forEach(msg -> {
                context.append("User: ")
                       .append(msg.getPrompt())
                       .append("\nAssistant: ")
                       .append(msg.getResponse())
                       .append("\n\n");
            });

        return context.toString();
    }
}

