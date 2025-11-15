package com.leo.ai.ollamachat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.leo.ai.ollamachat.repository")
public class OllamaChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(OllamaChatApplication.class, args);
    }
}

