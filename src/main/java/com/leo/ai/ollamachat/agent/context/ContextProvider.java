package com.leo.ai.ollamachat.agent.context;

import com.leo.ai.ollamachat.agent.context.model.ContextRequest;
import com.leo.ai.ollamachat.agent.context.model.ContextResponse;

public interface ContextProvider {

    /**
     * Resolve contexto relevante para um agente
     * a partir de um pedido estruturado.
     */
    ContextResponse resolve(ContextRequest request);
}

