package com.leo.ai.ollamachat.agent.plan;

import java.util.List;

public class AgentPlan {

    private List<String> steps;

    public AgentPlan(List<String> steps) {
        this.steps = steps;
    }

    public List<String> getSteps() {
        return steps;
    }
}
