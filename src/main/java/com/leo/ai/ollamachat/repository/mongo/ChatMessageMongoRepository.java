package com.leo.ai.ollamachat.repository.mongo;

import com.leo.ai.ollamachat.document.ChatMessageDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageMongoRepository
        extends MongoRepository<ChatMessageDocument, String> {

    Page<ChatMessageDocument> findBySessionIdOrderByCreatedAtDesc(
            String sessionId,
            Pageable pageable
    );
}




