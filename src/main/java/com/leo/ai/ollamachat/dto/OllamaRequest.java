package com.leo.ai.ollamachat.dto;

public class OllamaRequest {
    private String model;
    private String prompt;
    private Boolean stream;

    public OllamaRequest() {}

    public OllamaRequest(String model, String prompt, Boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public Boolean getStream() { return stream; }
    public void setStream(Boolean stream) { this.stream = stream; }
}
