package com.leo.ai.ollamachat.rag.retrieval;

import com.leo.ai.ollamachat.llm.client.OllamaChatClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MultiQueryGenerator {

    private final OllamaChatClient ollama;

    public MultiQueryGenerator(OllamaChatClient ollama) {
        this.ollama = ollama;
    }

    public List<String> generateQueries(String question) {

        String prompt = """
        Generate 4 alternative search queries for the following question.
        The queries should capture different semantic meanings.

        Question:
        %s

        Return one query per line.
        """.formatted(question);

        String response = ollama.chat(prompt);

        String[] lines = response.split("\\n");

        List<String> queries = new ArrayList<>();
        queries.add(question);

        for (String line : lines) {

            String q = line.trim();

            if (!q.isEmpty()) {
                queries.add(q);
            }
        }

        return queries;
    }
}
