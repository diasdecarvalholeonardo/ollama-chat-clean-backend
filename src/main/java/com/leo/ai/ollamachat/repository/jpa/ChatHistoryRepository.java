package com.leo.ai.ollamachat.repository.jpa;

import com.leo.ai.ollamachat.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository
        extends JpaRepository<ChatHistory, Long> {
}


