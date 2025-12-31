package com.leo.ai.ollamachat.agent.context.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;
import com.leo.ai.ollamachat.agent.context.provider.ContextProvider;

@Service
public class ContextService {

    private final ContextProvider contextProvider;

    public ContextService(
            @Qualifier("fakeContextProvider") ContextProvider contextProvider
        ) {
            this.contextProvider = contextProvider;
        }

    public ContextResponse resolveContext(ContextRequest request) {
        return contextProvider.resolve(request);
    }
}

