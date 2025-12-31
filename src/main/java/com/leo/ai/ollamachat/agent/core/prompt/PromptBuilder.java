package com.leo.ai.ollamachat.agent.core.prompt;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String build(PromptContext ctx) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI assistant.\n\n");

        prompt.append("Context Tags:\n")
              .append(ctx.getContext().tags())
              .append("\n\n");

        prompt.append("Context Content:\n")
              .append(ctx.getContext().content())
              .append("\n\n");

        prompt.append("User Input:\n")
              .append(ctx.getUserInput());

        return prompt.toString();
    }
}
