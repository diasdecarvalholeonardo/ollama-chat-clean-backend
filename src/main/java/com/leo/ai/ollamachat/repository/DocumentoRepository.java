package com.leo.ai.ollamachat.repository;

import com.leo.ai.ollamachat.model.Documento;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DocumentoRepository extends ReactiveMongoRepository<Documento, String> {
    Flux<Documento> findByAutor(String autor);
}

