package com.leo.ai.ollamachat.agent.tool;

import com.leo.ai.ollamachat.retrieval.service.WebSearchService;
import org.springframework.stereotype.Component;

@Component
public class WebSearchTool implements AgentTool {

    private final WebSearchService webSearchService;

    public WebSearchTool(WebSearchService webSearchService) {
        this.webSearchService = webSearchService;
    }

    /**
     * Nome usado pelo agente para invocar a tool
     */
    @Override
    public String getName() {
        return "webSearch";
    }

    /**
     * Descrição usada pelo agente / planner
     */
    @Override
    public String getDescription() {
        return "Search the internet for up-to-date information and return summarized results.";
    }

    /**
     * Executa a busca na web
     */
    @Override
    public String execute(String input) {

        if (input == null || input.isBlank()) {
            return "Web search query is empty.";
        }

        return webSearchService.search(input);

    }
}