package com.leo.ai.ollamachat.chatlog;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatInteractionRepository extends MongoRepository<ChatInteraction, String> {
}


