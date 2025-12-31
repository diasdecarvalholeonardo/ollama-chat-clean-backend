package com.leo.ai.ollamachat.agent.core.llm;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("fake")
public class FakeLLMClient implements LLMClient {

    @Override
    public String execute(String prompt) {
        return """
               [FAKE LLM RESPONSE]

               Prompt recebido:
               -----------------
               %s

               Resposta simulada gerada com sucesso.
               """.formatted(prompt);
    }
}

