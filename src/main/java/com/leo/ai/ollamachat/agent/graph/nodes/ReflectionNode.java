package com.leo.ai.ollamachat.agent.graph.nodes;

import com.leo.ai.ollamachat.agent.graph.AgentNode;
import com.leo.ai.ollamachat.agent.reflection.SelfReflectionAgentService;
import com.leo.ai.ollamachat.agent.state.AgentState;
import org.springframework.stereotype.Component;

@Component
public class ReflectionNode implements AgentNode {

    private final SelfReflectionAgentService reflectionAgent;

    public ReflectionNode(SelfReflectionAgentService reflectionAgent) {
        this.reflectionAgent = reflectionAgent;
    }

    @Override
    public AgentState execute(AgentState state) {

        String improved =
                reflectionAgent.reflect(
                        state.getQuestion(),
                        state.getAnswer()
                );

        state.setAnswer(improved);

        return state;
    }
}
