package com.leo.ai.ollamachat.agent.multi;

import com.leo.ai.ollamachat.model.router.ModelRouterService;
import org.springframework.stereotype.Service;

@Service
public class CodingAgent {

    private final ModelRouterService modelRouter;

    public CodingAgent(ModelRouterService modelRouter) {
        this.modelRouter = modelRouter;
    }

    public String handle(String question) {

        String prompt =
                "You are a programming assistant.\n\n"
                + question;

        return modelRouter.generate(prompt);

    }

}
