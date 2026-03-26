package com.leo.ai.ollamachat.agent.tool;

public interface Tool {

    String getName();

    String getDescription();

    String execute(String input);

}
