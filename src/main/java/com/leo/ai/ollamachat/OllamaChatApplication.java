package com.leo.ai.ollamachat;

import com.leo.ai.ollamachat.config.OllamaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableConfigurationProperties(OllamaProperties.class)
@ConfigurationPropertiesScan

// JPA (PostgreSQL + pgvector)
//@EnableJpaRepositories(basePackages = "com.leo.ai.ollamachat.domain.document")
//@EntityScan(basePackages = "com.leo.ai.ollamachat.domain.document")
@EnableJpaRepositories(basePackages = "com.leo.ai.ollamachat")
@EntityScan(basePackages = "com.leo.ai.ollamachat")

// MongoDB
@EnableMongoRepositories(basePackages = "com.leo.ai.ollamachat.persistence.mongo")

public class OllamaChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(OllamaChatApplication.class, args);
    }

}