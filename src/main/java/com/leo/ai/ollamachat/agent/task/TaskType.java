package com.leo.ai.ollamachat.agent.task;

public enum TaskType {

    // 🔹 Classificação semântica (intenção da pergunta)

    SIMPLE_QUESTION,
    KNOWLEDGE_RETRIEVAL,
    SUMMARIZATION,
    CODE_GENERATION,
    COMPLEX_REASONING,

    // 🔹 Classificação operacional (estratégia do sistema)

    CHAT,
    RAG,
    WEB,
    AGENT

}
