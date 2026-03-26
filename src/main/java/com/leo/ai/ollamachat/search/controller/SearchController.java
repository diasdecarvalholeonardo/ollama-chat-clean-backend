package com.leo.ai.ollamachat.search.controller;

import com.leo.ai.ollamachat.search.dto.SearchRequest;
import com.leo.ai.ollamachat.search.service.SearchService;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public List<Document> search(@RequestBody SearchRequest request) {
        return searchService.search(request.getQuery());
    }
}
