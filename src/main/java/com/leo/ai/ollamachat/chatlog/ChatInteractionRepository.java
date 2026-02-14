package com.leo.ai.ollamachat.chatlog;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatInteractionRepository
        extends MongoRepository<ChatInteraction, String> {
}




