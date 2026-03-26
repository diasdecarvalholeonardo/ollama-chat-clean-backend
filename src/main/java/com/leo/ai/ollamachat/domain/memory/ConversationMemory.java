package com.leo.ai.ollamachat.domain.memory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("memory")
public class ConversationMemory {

    @Id
    private String id;

    private String question;
    private String answer;

    private Instant createdAt;

    public ConversationMemory() {}

    public ConversationMemory(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.createdAt = Instant.now();
    }

    public String getQuestion() { return question; }

    public String getAnswer() { return answer; }
}
