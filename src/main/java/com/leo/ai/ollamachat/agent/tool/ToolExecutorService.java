package com.leo.ai.ollamachat.agent.tool;

import org.springframework.stereotype.Service;

@Service
public class ToolExecutorService {

    private final ToolRegistryService registry;

    public ToolExecutorService(ToolRegistryService registry) {
        this.registry = registry;
    }

    public String executeTool(String toolName, String input) {

    	AgentTool tool = registry.getTool(toolName);

        if (tool == null) {
            return "Tool not found: " + toolName;
        }

        return tool.execute(input);
    }
}
