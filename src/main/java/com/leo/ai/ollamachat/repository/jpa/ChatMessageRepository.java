package com.leo.ai.ollamachat.repository.jpa;

import com.leo.ai.ollamachat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {
}




