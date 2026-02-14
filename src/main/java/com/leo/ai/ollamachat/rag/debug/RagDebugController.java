package com.leo.ai.ollamachat.rag.debug;

import org.springframework.web.bind.annotation.*;

import com.leo.ai.ollamachat.persistence.mongo.RagDebugTraceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/rag/debug")
public class RagDebugController {

    private final RagDebugTraceRepository repository;

    public RagDebugController(RagDebugTraceRepository repository) {
        this.repository = repository;
    }

    /**
     * 🔍 Retorna o último trace salvo
     */
    @GetMapping("/latest")
    public RagDebugTraceView latest() {
        return repository
                .findTopByOrderByTimestampDesc()
                .map(RagDebugTraceView::from)
                .orElseThrow(() ->
                        new RuntimeException("Nenhum trace RAG encontrado"));
    }

    /**
     * 🔎 Retorna um trace específico por ID
     */
    @GetMapping("/{id}")
    public RagDebugTraceView byId(@PathVariable String id) {
        return repository
                .findById(id)
                .map(RagDebugTraceView::from)
                .orElseThrow(() ->
                        new RuntimeException("Trace RAG não encontrado"));
    }

    /**
     * 📜 Lista os últimos N traces (default 10)
     */
    @GetMapping
    public List<RagDebugTraceView> list(
            @RequestParam(defaultValue = "10") int limit
    ) {
        return repository.findTopN(limit)
                .stream()
                .map(RagDebugTraceView::from)
                .toList();
    }
}

