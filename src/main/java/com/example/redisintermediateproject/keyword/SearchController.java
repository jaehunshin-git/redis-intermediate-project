package com.example.redisintermediateproject.keyword;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping()
    public void search(@RequestParam String keyword) {
        searchService.search(keyword);
    }

    @GetMapping("/top10")
    public List<String> getTop10Keywords() {
        return searchService.getTop10Keywords();
    }

    @GetMapping("/redis")
    public void searchWithRedis(@RequestParam String keyword) {
        searchService.searchWithRedis(keyword);
    }

    @GetMapping("/top10/redis")
    public List<String> geteTop10KeywordsWithRedis() {
        return searchService.getTop10KeywordsWithRedis();
    }
}
