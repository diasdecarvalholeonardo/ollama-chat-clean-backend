package com.leo.ai.ollamachat.agent.plan;

import com.leo.ai.ollamachat.model.router.ModelRouterService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanningAgentService {

    private final ModelRouterService modelRouter;

    public PlanningAgentService(ModelRouterService modelRouter) {
        this.modelRouter = modelRouter;
    }

    /**
     * Método principal (já existente no seu projeto)
     */
    public AgentPlan createPlan(String question) {

        String prompt = """
You are an AI planning agent.

Break the user request into a short list of steps.

Return the plan as numbered steps.

User Question:
""" + question;

        String response = modelRouter.generate(prompt);

        List<String> steps = Arrays.stream(response.split("\n"))
                .filter(line -> line.matches("\\d+.*"))
                .collect(Collectors.toList());

        return new AgentPlan(steps);
    }

    /**
     * Método auxiliar para compatibilidade com Orchestrator
     */
    public String plan(String question) {

        AgentPlan plan = createPlan(question);

        return plan.getSteps()
                .stream()
                .collect(Collectors.joining("\n"));

    }
}