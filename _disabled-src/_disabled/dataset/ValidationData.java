package com.leo.ai.ollamachat.dataset.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "validation_data")
public class ValidationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT")
    private String context;

    @Column(columnDefinition = "TEXT")
    private String expectedAnswer;

    @Column
    private String category;

    public ValidationData() {}

    public ValidationData(String question, String context, String expectedAnswer, String category) {
        this.question = question;
        this.context = context;
        this.expectedAnswer = expectedAnswer;
        this.category = category;
    }

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public String getExpectedAnswer() { return expectedAnswer; }
    public void setExpectedAnswer(String expectedAnswer) { this.expectedAnswer = expectedAnswer; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

