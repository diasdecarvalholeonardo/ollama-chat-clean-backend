package com.leo.ai.ollamachat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/api/test/ollama")
    public Object testOllama() {

        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:11434/api/tags";

        return restTemplate.getForObject(url, Map.class);
    }
}
