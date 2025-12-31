package com.leo.ai.ollamachat.agent.context.service;

import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;
import com.leo.ai.ollamachat.agent.context.provider.ContextProvider;
import org.springframework.stereotype.Service;

@Service
public class AgentContextService {

    private final ContextProvider contextProvider;

    public AgentContextService(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    public ContextResponse resolve(ContextRequest request) {
        return contextProvider.resolve(request);
    }
}


