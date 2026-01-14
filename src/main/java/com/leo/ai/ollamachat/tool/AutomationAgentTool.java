package com.leo.ai.ollamachat.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Serviço responsável por disparar fluxos de automação
 * no n8n a partir do backend Spring Boot.
 *
 * NÃO usa Spring AI Tool (@Tool)
 */
@Service
public class AutomationAgentTool {

    private final WebClient webClient;
    private final String n8nWebhookPath = "/webhook/trigger-sales";

    public AutomationAgentTool(
            WebClient.Builder webClientBuilder,
            @Value("${tool.automation.n8n-base-url}") String n8nBaseUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(n8nBaseUrl)
                .build();
    }

    public String triggerSalesWorkflow(
            String leadName,
            String leadEmail,
            String messageContent
    ) {
        try {
            webClient.post()
                    .uri(n8nWebhookPath)
                    .bodyValue(new Payload(leadName, leadEmail, messageContent))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return "Workflow de vendas disparado com sucesso para " + leadEmail;

        } catch (Exception e) {
            return "Erro ao disparar workflow no n8n: " + e.getMessage();
        }
    }

    private record Payload(
            String leadName,
            String leadEmail,
            String messageContent
    ) {}
}
