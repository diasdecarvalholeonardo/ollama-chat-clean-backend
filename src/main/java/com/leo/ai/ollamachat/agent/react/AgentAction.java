package com.leo.ai.ollamachat.agent.react;

public class AgentAction {

    private String tool;
    private String input;

    public AgentAction() {}

    public AgentAction(String tool, String input) {
        this.tool = tool;
        this.input = input;
    }

    public String getTool() {
        return tool;
    }

    public String getInput() {
        return input;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
