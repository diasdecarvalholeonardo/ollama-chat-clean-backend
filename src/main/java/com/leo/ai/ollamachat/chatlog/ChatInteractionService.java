package com.leo.ai.ollamachat.chatlog;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class ChatInteractionService {

    private final ChatInteractionRepository repository;

    public ChatInteractionService(ChatInteractionRepository repository) {
        this.repository = repository;
    }

    public ChatInteraction save(ChatInteraction interaction) {
        // Mongo gera o id automaticamente se for null
        return repository.save(interaction);
    }

    public Iterable<ChatInteraction> findAll() {
        return repository.findAll();
    }
}


