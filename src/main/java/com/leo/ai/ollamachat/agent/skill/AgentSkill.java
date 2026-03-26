package com.leo.ai.ollamachat.agent.skill;

public interface AgentSkill {

    String getName();

    String getDescription();

    String execute(String input);
}
