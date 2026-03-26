package com.leo.ai.ollamachat.agent.graph;

import com.leo.ai.ollamachat.agent.state.AgentState;

public interface AgentNode {

    AgentState execute(AgentState state);

}
