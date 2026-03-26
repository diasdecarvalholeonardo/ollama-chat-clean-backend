package com.leo.ai.ollamachat.ingestion;

import com.leo.ai.ollamachat.OllamaChatApplication;
import com.leo.ai.ollamachat.ingestion.service.IngestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = OllamaChatApplication.class)
@ActiveProfiles("test")
public class IngestionTest {

    @Autowired
    private IngestionService ingestionService;

    @Test
    void testIngestion() {

        String texto = """
        O Ollama Chat Clean usa RAG com pgvector para busca eficiente.
        Esse sistema combina embeddings e recuperação semântica.
        Ele utiliza chunking para dividir textos grandes.
        """;

        for (int i = 0; i < 50; i++) {
            ingestionService.ingestText(texto);
        }

        System.out.println("Ingestão finalizada!");
    }
}
