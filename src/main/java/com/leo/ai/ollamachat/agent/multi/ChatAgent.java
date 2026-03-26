package com.leo.ai.ollamachat.agent.multi;

import com.leo.ai.ollamachat.model.router.ModelRouterService;
import org.springframework.stereotype.Service;

@Service
public class ChatAgent {

    private final ModelRouterService modelRouter;

    public ChatAgent(ModelRouterService modelRouter) {
        this.modelRouter = modelRouter;
    }

    public String handle(String question) {

        return modelRouter.generate(question);

    }

}
