package com.leo.ai.ollamachat;

import com.leo.ai.ollamachat.config.OllamaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(OllamaProperties.class)
@ConfigurationPropertiesScan

// ✅ CORREÇÃO AQUI
@EnableJpaRepositories(basePackages = "com.leo.ai.ollamachat")

// ✅ Entidades JPA
@EntityScan(basePackages = "com.leo.ai.ollamachat")

// ✅ Mongo continua separado (correto)
@EnableMongoRepositories(basePackages = {
        "com.leo.ai.ollamachat.memory.repository",
        "com.leo.ai.ollamachat.persistence.mongo"
})

public class OllamaChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(OllamaChatApplication.class, args);
    }

}