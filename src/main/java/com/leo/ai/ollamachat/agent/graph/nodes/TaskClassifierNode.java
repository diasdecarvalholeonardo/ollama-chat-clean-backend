package com.leo.ai.ollamachat.agent.graph.nodes;

import com.leo.ai.ollamachat.agent.graph.AgentNode;
import com.leo.ai.ollamachat.agent.state.AgentState;
import com.leo.ai.ollamachat.agent.task.TaskClassifierService;
import com.leo.ai.ollamachat.agent.task.TaskType;
import org.springframework.stereotype.Component;

@Component
public class TaskClassifierNode implements AgentNode {

    private final TaskClassifierService classifier;

    public TaskClassifierNode(TaskClassifierService classifier) {
        this.classifier = classifier;
    }

    @Override
    public AgentState execute(AgentState state) {

        TaskType type = classifier.classify(state.getQuestion());

        state.setTaskType(type.name());

        return state;
    }
}
