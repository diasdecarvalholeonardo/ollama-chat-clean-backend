package com.leo.ai.ollamachat.llm;

public interface LLMClient {

    String generate(String prompt);
    String generate(String prompt, String systemPrompt);
}
