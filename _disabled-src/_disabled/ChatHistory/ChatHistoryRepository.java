package com.leo.ai.ollamachat.repository;

import com.leo.ai.ollamachat.model.ChatHistory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ChatHistoryRepository extends ReactiveCrudRepository<ChatHistory, Long> {
}

