package com.leo.ai.ollamachat.agent.core.prompt;

import com.leo.ai.ollamachat.agent.context.model.ContextResponse;

public class PromptContext {

    private final String userInput;
    private final ContextResponse context;

    public PromptContext(String userInput, ContextResponse context) {
        this.userInput = userInput;
        this.context = context;
    }

    public String getUserInput() {
        return userInput;
    }

    public ContextResponse getContext() {
        return context;
    }
}

