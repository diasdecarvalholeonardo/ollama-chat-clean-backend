package com.leo.ai.ollamachat._disabled.mongo;

import com.leo.ai.ollamachat.model.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório reativo para salvar e consultar mensagens do chat no MongoDB.
 */
@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
}




