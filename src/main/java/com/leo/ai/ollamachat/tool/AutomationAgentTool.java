package com.leo.ai.ollamachat.tool;

import org.springframework.stereotype.Component;

@Component
public class AutomationAgentTool {

    public String checkSystemHealth() {
        return "Backend is running and healthy.";
    }

    public String echo(String message) {
        return "Echo: " + message;
    }
}
