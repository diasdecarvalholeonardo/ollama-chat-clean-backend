package com.leo.ai.ollamachat.agent.memory;

import org.springframework.stereotype.Service;

@Service
public class AgentScratchpadService {

    public AgentScratchpad create() {
        return new AgentScratchpad();
    }

}
