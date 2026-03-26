package com.leo.ai.ollamachat.retrieval.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Service
public class WebSearchService {

    private final RestTemplate restTemplate;

    public WebSearchService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Busca informações externas quando o RAG local não encontra resposta.
     * Atualmente usa Wikipedia API.
     */
    public String search(String query) {

        String wikipediaResult = searchWikipedia(query);

        if (wikipediaResult != null && !wikipediaResult.isBlank()) {
            return wikipediaResult;
        }

        return null;
    }

    /**
     * Busca resumo na Wikipedia
     */
    private String searchWikipedia(String query) {

        try {

            String formattedQuery =
                    query.trim().replace(" ", "%20");

            String url =
                    "https://en.wikipedia.org/api/rest_v1/page/summary/"
                            + formattedQuery;

            ResponseEntity<Map> response =
                    restTemplate.getForEntity(url, Map.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                return null;
            }

            Map body = response.getBody();

            if (body == null) {
                return null;
            }

            Object extract = body.get("extract");

            if (extract == null) {
                return null;
            }

            return extract.toString();

        } catch (Exception e) {

            System.out.println("Web search failed: " + e.getMessage());

            return null;

        }

    }
}
