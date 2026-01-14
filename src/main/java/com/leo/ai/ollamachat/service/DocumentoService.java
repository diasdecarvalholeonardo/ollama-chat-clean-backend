package com.leo.ai.ollamachat.service;

import com.leo.ai.ollamachat.model.Documento;
import com.leo.ai.ollamachat.repository.mongo.DocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private final DocumentoRepository repository;

    public DocumentoService(DocumentoRepository repository) {
        this.repository = repository;
    }

    public Documento salvar(Documento documento) {
        return repository.save(documento);
    }

    public List<Documento> listarTodos() {
        return repository.findAll();
    }

    public Optional<Documento> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Documento> buscarPorAutor(String autor) {
        return repository.findByAutor(autor);
    }

    public void deletar(String id) {
        repository.deleteById(id);
    }
}
