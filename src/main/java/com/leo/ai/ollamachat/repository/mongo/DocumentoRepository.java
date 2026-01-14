package com.leo.ai.ollamachat.repository.mongo;

import com.leo.ai.ollamachat.model.Documento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentoRepository
        extends MongoRepository<Documento, String> {

    List<Documento> findByAutor(String autor);
}

