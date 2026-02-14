package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.model.Documento;
import com.leo.ai.ollamachat.service.DocumentoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Profile;
import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@Profile("prod")
public class DocumentoController {

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @PostMapping
    public Documento criar(@RequestBody Documento documento) {
        return null;
    }

    @GetMapping
    public List<Documento> listar() {
        return null;
    }

    @GetMapping("/autor/{autor}")
    public List<Documento> buscarPorAutor(@PathVariable String autor) {
        return null;
    }
}
