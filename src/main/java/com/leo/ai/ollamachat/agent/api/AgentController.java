package com.leo.ai.ollamachat.agent.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leo.ai.ollamachat.agent.core.ContextAwareAgent;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final ContextAwareAgent agent;

    public AgentController(ContextAwareAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String userInput) {
        return agent.handle(userInput);
    }
}
