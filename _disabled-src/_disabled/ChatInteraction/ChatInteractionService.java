package com.leo.ai.ollamachat._disabled;

import org.springframework.stereotype.Service;

@Service
public class ChatInteractionService {

    private final ChatInteractionRepository repository;

    public ChatInteractionService(ChatInteractionRepository repository) {
        this.repository = repository;
    }

    public ChatInteraction save(ChatInteraction interaction) {
        return repository.save(interaction);
    }

    public Iterable<ChatInteraction> findAll() {
        return repository.findAll();
    }
}
