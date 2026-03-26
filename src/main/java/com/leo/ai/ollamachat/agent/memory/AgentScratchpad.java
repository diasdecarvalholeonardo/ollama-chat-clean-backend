package com.leo.ai.ollamachat.agent.memory;

import java.util.ArrayList;
import java.util.List;

public class AgentScratchpad {

    private final List<String> steps = new ArrayList<>();

    public void addThought(String thought) {
        steps.add("Thought: " + thought);
    }

    public void addAction(String action) {
        steps.add("Action: " + action);
    }

    public void addObservation(String observation) {
        steps.add("Observation: " + observation);
    }

    public String buildContext() {
        return String.join("\n", steps);
    }

}