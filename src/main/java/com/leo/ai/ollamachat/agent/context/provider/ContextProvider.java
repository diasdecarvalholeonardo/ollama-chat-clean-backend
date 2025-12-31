package com.leo.ai.ollamachat.agent.context.provider;

import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;

public interface ContextProvider {

    ContextResponse resolve(ContextRequest request);

}


