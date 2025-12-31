package com.leo.ai.ollamachat.controller;

import com.leo.ai.ollamachat.service.AutomationAgentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
public class AutomationAgentController {

    private final AutomationAgentService agentService;

    public AutomationAgentController(AutomationAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/run")
    public ResponseEntity<Map<String, String>> runAgent(
            @RequestBody Map<String, String> body
    ) {
        String prompt = body.get("prompt");

        String result = agentService.runAgent(prompt);

        return ResponseEntity.ok(
                Map.of("result", result)
        );
    }
}

