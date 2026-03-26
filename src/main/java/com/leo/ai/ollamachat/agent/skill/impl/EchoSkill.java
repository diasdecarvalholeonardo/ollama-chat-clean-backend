package com.leo.ai.ollamachat.agent.skill.impl;

import com.leo.ai.ollamachat.agent.skill.AgentSkill;
import org.springframework.stereotype.Component;

@Component
public class EchoSkill implements AgentSkill {

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getDescription() {
        return "Echo the input text";
    }

    @Override
    public String execute(String input) {

        return "Echo response: " + input;

    }
}
