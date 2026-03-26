package com.leo.ai.ollamachat.agent.state;

import java.util.HashMap;
import java.util.Map;

public class AgentState {

    private String question;
    private String answer;
    private String taskType;

    private final Map<String, Object> context = new HashMap<>();

    public AgentState(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Map<String, Object> getContext() {
        return context;
    }
}
