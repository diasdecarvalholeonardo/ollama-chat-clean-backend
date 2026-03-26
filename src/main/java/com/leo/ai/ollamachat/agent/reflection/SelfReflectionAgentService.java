package com.leo.ai.ollamachat.agent.reflection;

import com.leo.ai.ollamachat.model.router.ModelRouterService;
import org.springframework.stereotype.Service;

@Service
public class SelfReflectionAgentService {

    private final ModelRouterService modelRouter;

    public SelfReflectionAgentService(ModelRouterService modelRouter) {
        this.modelRouter = modelRouter;
    }

    public String reflect(String question, String answer) {

        String prompt = """
You are a critic AI.

Evaluate the quality of the answer.

If the answer is correct and clear, return it unchanged.

If the answer is incomplete or incorrect, improve it.

User Question:
""" + question + """

Original Answer:
""" + answer + """

Improved Answer:
""";

        return modelRouter.generate(prompt);
    }
}
