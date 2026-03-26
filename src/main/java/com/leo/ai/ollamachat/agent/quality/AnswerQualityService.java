package com.leo.ai.ollamachat.agent.quality;

import com.leo.ai.ollamachat.model.router.ModelRouterService;
import org.springframework.stereotype.Service;

@Service
public class AnswerQualityService {

    private final ModelRouterService modelRouter;

    public AnswerQualityService(ModelRouterService modelRouter) {
        this.modelRouter = modelRouter;
    }

    public boolean isAnswerGood(String question, String answer) {

        String prompt = """
You are an AI judge.

Evaluate the answer quality.

Return ONLY:

GOOD
or
BAD

User Question:
""" + question + """

Answer:
""" + answer;

        String result = modelRouter.generate(prompt).trim();

        return result.equalsIgnoreCase("GOOD");
    }
}
