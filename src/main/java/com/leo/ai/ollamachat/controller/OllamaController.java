package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.dto.ChatRequest;
import com.leo.ai.ollamachat.service.OllamaService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ollama")
@CrossOrigin(origins = "*")
public class OllamaController {

    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    @PostMapping("/ask")
    public Mono<Map<String, Object>> askOllama(@RequestBody ChatRequest request) {
        final String GREEN = "\u001B[32m";
        final String RED = "\u001B[31m";
        final String RESET = "\u001B[0m";

        System.out.println(GREEN + "[DEBUG] Requisição recebida: " + request.getMessage() + RESET);

        return ollamaService.askOllama(request.getMessage())
                .map(response -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("response", response);
                    System.out.println(GREEN + "[SUCESSO] Resposta recebida do Ollama." + RESET);
                    return result;
                })
                .onErrorResume(e -> {
                    System.err.println(RED + "[ERRO CONTROLLER] " + e.getMessage() + RESET);
                    Map<String, Object> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    return Mono.just(error);
                });
    }
}
