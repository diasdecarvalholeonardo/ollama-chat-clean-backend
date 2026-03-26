package com.leo.ai.ollamachat.agent.task;

import org.springframework.stereotype.Service;

@Service
public class TaskClassifierService {

    public TaskType classify(String question) {

        String q = question.toLowerCase();

        // 🌐 Web queries
        if (q.contains("news")
                || q.contains("latest")
                || q.contains("today")) {

            return TaskType.WEB;
        }

        // 📚 Knowledge retrieval
        if (q.contains("document")
                || q.contains("knowledge")
                || q.contains("internal")) {

            return TaskType.KNOWLEDGE_RETRIEVAL;
        }

        // 💻 Code generation
        if (q.contains("code")
                || q.contains("java")
                || q.contains("python")) {

            return TaskType.CODE_GENERATION;
        }

        // 🧠 Complex reasoning
        if (q.contains("explain")
                || q.contains("analyze")
                || q.contains("compare")) {

            return TaskType.COMPLEX_REASONING;
        }

        // 🧾 Summarization
        if (q.contains("summarize")
                || q.contains("summary")) {

            return TaskType.SUMMARIZATION;
        }

        return TaskType.SIMPLE_QUESTION;
    }

}
