package com.leo.ai.ollamachat.rag.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class QueryRewriteService {

    private final ChatModel chatModel;

    public QueryRewriteService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String rewrite(String query) {

        String prompt = """
                Rewrite the user query to improve search quality.

                RULES:
                - Keep original intent
                - Expand abbreviations
                - Make it more specific
                - Do not explain

                Query:
                %s

                Rewritten query:
                """.formatted(query);

        try {
            return chatModel.call(new Prompt(prompt))
                    .getResult()
                    .getOutput()
                    .getContent()
                    .trim();
        } catch (Exception e) {
            return query;
        }
    }
}
