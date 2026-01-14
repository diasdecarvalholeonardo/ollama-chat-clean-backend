package com.leo.ai.ollamachat;

import com.leo.ai.ollamachat.config.OllamaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties(OllamaProperties.class)

// JPA → PostgreSQL (APENAS repositórios JPA)
@EnableJpaRepositories(basePackages = {
        "com.leo.ai.ollamachat.repository.jpa"
})

// Mongo → Documentos + Chat / Memória
@EnableMongoRepositories(basePackages = {
        "com.leo.ai.ollamachat.repository.mongo",
        "com.leo.ai.ollamachat.chatlog"
})
public class OllamaChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(OllamaChatApplication.class, args);
    }
}
