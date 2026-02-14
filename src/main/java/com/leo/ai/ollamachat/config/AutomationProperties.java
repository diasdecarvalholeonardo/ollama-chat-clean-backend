package com.leo.ai.ollamachat.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tool.automation")
public class AutomationProperties {

    private String n8nBaseUrl;

    public String getN8nBaseUrl() {
        return n8nBaseUrl;
    }

    public void setN8nBaseUrl(String n8nBaseUrl) {
        this.n8nBaseUrl = n8nBaseUrl;
    }
}

