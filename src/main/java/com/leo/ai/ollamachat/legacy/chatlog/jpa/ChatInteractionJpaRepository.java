package com.leo.ai.ollamachat.legacy.chatlog.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.leo.ai.ollamachat.chatlog.ChatInteraction;

@Repository
public interface ChatInteractionJpaRepository extends JpaRepository<ChatInteraction, String> {
}



