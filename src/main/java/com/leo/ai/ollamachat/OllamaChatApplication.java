package com.leo.ai.ollamachat;

import com.leo.ai.ollamachat.config.OllamaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@EnableConfigurationProperties(OllamaProperties.class)
@EnableJpaRepositories(basePackages = "com.leo.ai.ollamachat.persistence.jpa")
@EnableMongoRepositories(basePackages = "com.leo.ai.ollamachat.persistence.mongo")
@ConfigurationPropertiesScan

public class OllamaChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(OllamaChatApplication.class, args);
    }
}
