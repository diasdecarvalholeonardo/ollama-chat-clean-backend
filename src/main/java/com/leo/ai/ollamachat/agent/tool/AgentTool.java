package com.leo.ai.ollamachat.agent.tool;

public interface AgentTool {

    String getName();
    String getDescription();
    String execute(String input);

}
