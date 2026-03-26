package com.leo.ai.ollamachat.agent;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class AgentPlannerService {

    private final ChatClient chatClient;

    public AgentPlannerService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public List<String> plan(String question) {

        String prompt = """
        Break the user question into smaller search queries.

        Return each query in a new line.

        Question:
        %s
        """.formatted(question);

        String result =
                chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();

        return Arrays.stream(result.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
