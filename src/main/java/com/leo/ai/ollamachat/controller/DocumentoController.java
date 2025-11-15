package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.model.Documento;
import com.leo.ai.ollamachat.service.DocumentoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @PostMapping
    public Mono<Documento> salvar(@RequestBody Documento doc) {
        return service.salvar(doc);
    }

    @GetMapping
    public Flux<Documento> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/autor/{autor}")
    public Flux<Documento> buscarPorAutor(@PathVariable String autor) {
        return service.buscarPorAutor(autor);
    }
}


