package com.leo.ai.ollamachat.agent.core.llm;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Component
@Profile("ollama")
public class OllamaLLMClient implements LLMClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String OLLAMA_URL =
            "http://localhost:11434/api/generate";

    private static final String MODEL = "llama3";

    @Override
    public String execute(String prompt) {

        Map<String, Object> request = Map.of(
                "model", MODEL,
                "prompt", prompt,
                "stream", false
        );

        Map response =
                restTemplate.postForObject(
                        OLLAMA_URL,
                        request,
                        Map.class
                );

        return response.get("response").toString();
    }
}

