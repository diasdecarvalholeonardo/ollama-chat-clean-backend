package com.leo.ai.ollamachat;

//import com.leo.ai.ollamachat.config.OllamaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
//import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication(
	    scanBasePackages = {
	        "com.leo.ai.ollamachat.agent",
	        "com.leo.ai.ollamachat.tool",
	        "com.leo.ai.ollamachat.service",
	        "com.leo.ai.ollamachat.controller"
	    }
	)
//@EnableConfigurationProperties(OllamaProperties.class)
//@EnableReactiveMongoRepositories(basePackages = "com.leo.ai.ollamachat.repository")
public class OllamaChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(OllamaChatApplication.class, args);
    }
    
    
    
}
