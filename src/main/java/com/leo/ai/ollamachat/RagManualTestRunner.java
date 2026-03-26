package com.leo.ai.ollamachat;

import com.leo.ai.ollamachat.rag.service.RagResponse;
import com.leo.ai.ollamachat.rag.service.RagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class RagManualTestRunner implements CommandLineRunner {

    private final RagService ragService;

    public RagManualTestRunner(RagService ragService) {
        this.ragService = ragService;
    }

    @Override
    public void run(String... args) {

        System.out.println("\n🚀 TESTE RAG INICIADO\n");

        RagResponse response = ragService.ask(
                "Qual é o objetivo principal deste documento?",
                5,
                false
        );

        System.out.println("🧠 RESPOSTA:");
        System.out.println(response.getAnswer());

        System.out.println("\n📚 FONTES UTILIZADAS:");
        response.getSources().forEach(source ->
                System.out.println("- " + source.getId())
        );

        System.out.println("\n✅ TESTE RAG FINALIZADO\n");
    }
}

