package com.leo.ai.ollamachat.agent.service;

import com.leo.ai.ollamachat.agent.tool.AgentTool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgentToolRegistry {

    private final Map<String, AgentTool> tools;

    public AgentToolRegistry(List<AgentTool> toolList) {

        this.tools =
                toolList.stream()
                        .collect(Collectors.toMap(
                                AgentTool::getName,
                                tool -> tool
                        ));
    }

    public AgentTool getTool(String name) {
        return tools.get(name);
    }

    public Map<String, AgentTool> getAllTools() {
        return tools;
    }
}
