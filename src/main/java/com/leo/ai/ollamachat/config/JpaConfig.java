package com.leo.ai.ollamachat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "com.leo.ai.ollamachat.chatlog.jpa",
                "com.leo.ai.ollamachat.rag.debug"
        }
)
@EntityScan(
        basePackages = {
                "com.leo.ai.ollamachat.chatlog",
                "com.leo.ai.ollamachat.rag.debug"
        }
)
public class JpaConfig {
}

