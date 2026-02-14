package com.leo.ai.ollamachat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = {
                "com.leo.ai.ollamachat.repository.mongo",
                "com.leo.ai.ollamachat.ingestion.repository"
        }
)
public class MongoConfig {
}

