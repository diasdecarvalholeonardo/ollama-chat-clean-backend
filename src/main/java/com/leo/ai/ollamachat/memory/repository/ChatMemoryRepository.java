package com.leo.ai.ollamachat.memory.repository;

import com.leo.ai.ollamachat.memory.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChatMemoryRepository
        extends MongoRepository<ChatMessage, String> {

    List<ChatMessage> findBySessionIdOrderByTimestampAsc(String sessionId);

}
