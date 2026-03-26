package com.leo.ai.ollamachat.agent.test;

import com.leo.ai.ollamachat.agent.tool.ToolExecutorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SkillTestController {

    private final ToolExecutorService toolExecutor;

    public SkillTestController(ToolExecutorService toolExecutor) {
        this.toolExecutor = toolExecutor;
    }

    @GetMapping("/api/test/skill")
    public String testSkill(
            @RequestParam String tool,
            @RequestParam String input) {

        return toolExecutor.executeTool(tool, input);
    }
}
