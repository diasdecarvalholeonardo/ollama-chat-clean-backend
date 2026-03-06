package com.leo.ai.ollamachat.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.leo.ai.ollamachat.persistence.mongo.debug.RagDebugTraceDocument;

import java.util.List;
import java.util.Optional;

public interface RagDebugTraceRepository2
        extends MongoRepository<RagDebugTraceDocument, String> {

    Optional<RagDebugTraceDocument> findTopByOrderByTimestampDesc();

    @Query("{}")
    List<RagDebugTraceDocument> findTopN(int limit);
}


