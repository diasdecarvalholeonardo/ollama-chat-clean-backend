package com.leo.ai.ollamachat.controller.document;

import org.springframework.web.bind.annotation.*;

import com.leo.ai.ollamachat.domain.document.DocumentChunk;
import com.leo.ai.ollamachat.service.document.DocumentChunkService;

import java.util.List;

@RestController
@RequestMapping("/api/document-chunks")
public class DocumentChunkController {

    private final DocumentChunkService service;

    public DocumentChunkController(DocumentChunkService service) {
        this.service = service;
    }

    @PostMapping
    public DocumentChunk create(@RequestBody DocumentChunk chunk) {
        return service.save(chunk);
    }

    @GetMapping
    public List<DocumentChunk> list() {
        return service.findAll();
    }
}