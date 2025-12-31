package com.leo.ai.ollamachat._disabled;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatlog")
public class ChatInteractionController {

    private final ChatInteractionService service;

    public ChatInteractionController(ChatInteractionService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ChatInteraction save(@RequestBody ChatInteraction interaction) {
        return service.save(interaction);
    }

    @GetMapping("/all")
    public Iterable<ChatInteraction> getAll() {
        return service.findAll();
    }
}
