package com.develetter.develetter.blog.controller;


import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService SearchService) {
        this.searchService = SearchService;
    }

    @GetMapping("/search")
    public String searchAndSave(@RequestParam String query) {
        searchService.searchAndSaveBlogPosts(query);
        return "검색 데이터가 성공적으로 저장되었습니다!";
    }

    @GetMapping("/blogs")
    public List<Blog> getAllBlogs() {
        return searchService.getAllBlogs();
    }

}

