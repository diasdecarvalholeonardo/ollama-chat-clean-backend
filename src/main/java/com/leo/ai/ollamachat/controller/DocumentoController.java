package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.model.Documento;
import com.leo.ai.ollamachat.service.DocumentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    private final DocumentoService service;

    public DocumentoController(DocumentoService service) {
        this.service = service;
    }

    @PostMapping
    public Documento criar(@RequestBody Documento documento) {
        return service.salvar(documento);
    }

    @GetMapping
    public List<Documento> listar() {
        return service.listarTodos();
    }

    @GetMapping("/autor/{autor}")
    public List<Documento> buscarPorAutor(@PathVariable String autor) {
        return service.buscarPorAutor(autor);
    }
}
