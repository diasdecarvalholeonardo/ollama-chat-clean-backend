package com.leo.ai.ollamachat.agent.graph.nodes;

import com.leo.ai.ollamachat.agent.graph.AgentNode;
import com.leo.ai.ollamachat.agent.react.ReActAgentService;
import com.leo.ai.ollamachat.agent.state.AgentState;
import org.springframework.stereotype.Component;

@Component
public class ReActNode implements AgentNode {

    private final ReActAgentService reactAgent;

    public ReActNode(ReActAgentService reactAgent) {
        this.reactAgent = reactAgent;
    }

    @Override
    public AgentState execute(AgentState state) {

        String answer = reactAgent.runAgent(state.getQuestion());

        state.setAnswer(answer);

        return state;
    }
}
