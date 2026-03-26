package com.leo.ai.ollamachat.agent.multi;

import com.leo.ai.ollamachat.model.router.ModelRouterService;
import org.springframework.stereotype.Service;

@Service
public class LLMSupervisorService {

    private final ModelRouterService modelRouter;

    public LLMSupervisorService(ModelRouterService modelRouter) {
        this.modelRouter = modelRouter;
    }

    public AgentType decideAgent(String question) {

        String prompt = """
You are an AI supervisor.

Decide which agent should handle the request.

Available agents:

CHAT
RAG
CODING
WEB

Respond ONLY with the agent name.

User Question:
""" + question;

        String response = modelRouter.generate(prompt);

        try {
            return AgentType.valueOf(response.trim().toUpperCase());
        } catch (Exception e) {
            return AgentType.CHAT;
        }
    }
}
