package com.leo.ai.ollamachat.service;

import com.leo.ai.ollamachat.model.Documento;
import com.leo.ai.ollamachat.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DocumentoService {

    private final DocumentoRepository repository;

    public DocumentoService(DocumentoRepository repository) {
        this.repository = repository;
    }

    public Mono<Documento> salvar(Documento doc) {
        return repository.save(doc);
    }

    public Flux<Documento> listarTodos() {
        return repository.findAll();
    }

    public Flux<Documento> buscarPorAutor(String autor) {
        return repository.findByAutor(autor);
    }
}

