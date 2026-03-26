package com.leo.ai.ollamachat.agent.multi;

import org.springframework.stereotype.Service;

@Service
public class SupervisorAgent {

    private final ChatAgent chatAgent;
    private final RAGAgent ragAgent;
    private final CodingAgent codingAgent;
    private final LLMSupervisorService llmSupervisor;

    public SupervisorAgent(
            ChatAgent chatAgent,
            RAGAgent ragAgent,
            CodingAgent codingAgent,
            LLMSupervisorService llmSupervisor) {

        this.chatAgent = chatAgent;
        this.ragAgent = ragAgent;
        this.codingAgent = codingAgent;
        this.llmSupervisor = llmSupervisor;
    }

    public String route(String question) {

        AgentType agentType = llmSupervisor.decideAgent(question);

        switch (agentType) {

            case RAG:
                return ragAgent.handle(question);

            case CODING:
                return codingAgent.handle(question);

            case CHAT:
            default:
                return chatAgent.handle(question);
        }
    }
}