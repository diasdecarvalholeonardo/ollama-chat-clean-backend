package com.leo.ai.ollamachat.model.router;

import com.leo.ai.ollamachat.agent.task.TaskClassifierService;
import com.leo.ai.ollamachat.agent.task.TaskType;
import com.leo.ai.ollamachat.model.service.OllamaLLMService;
import com.leo.ai.ollamachat.model.service.OpenAiLLMService;
import org.springframework.stereotype.Service;

@Service
public class ModelRouterService {

    private final OllamaLLMService ollama;
    private final OpenAiLLMService openai;
    private final TaskClassifierService classifier;

    public ModelRouterService(
            OllamaLLMService ollama,
            OpenAiLLMService openai,
            TaskClassifierService classifier) {

        this.ollama = ollama;
        this.openai = openai;
        this.classifier = classifier;
    }

    public String generate(String prompt) {

        TaskType task = classifier.classify(prompt);

        switch (task) {

            case SIMPLE_QUESTION:
            case KNOWLEDGE_RETRIEVAL:
                return ollama.generate(prompt);

            case SUMMARIZATION:
                return ollama.generate(prompt);

            case CODE_GENERATION:
            case COMPLEX_REASONING:
                return openai.generate(prompt);

            default:
                return ollama.generate(prompt);
        }
    }
}