package com.leo.ai.ollamachat.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.leo.ai.ollamachat.config.AutomationProperties;

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
    private final AutomationProperties automationProperties;

    public AutomationAgentTool(AutomationProperties automationProperties, WebClient webClient) {
        this.webClient = webClient;
		this.automationProperties = automationProperties;
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
