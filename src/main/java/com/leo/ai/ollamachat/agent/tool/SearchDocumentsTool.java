package com.leo.ai.ollamachat.agent.tool;

import com.leo.ai.ollamachat.rag.service.RagService;
import org.springframework.stereotype.Component;

@Component
public class SearchDocumentsTool implements AgentTool {

    private final RagService ragService;

    public SearchDocumentsTool(RagService ragService) {
        this.ragService = ragService;
    }

    @Override
    public String getName() {
        return "searchDocuments";
    }

    @Override
    public String getDescription() {
        return "Search the internal knowledge base for documents.";
    }

    @Override
    public String execute(String input) {

        return ragService.ask(input, 5, false).getAnswer();

    }
}


