package com.develetter.develetter.blog;

import com.develetter.develetter.blog.service.SearchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BlogDataLoader implements CommandLineRunner {

    private final SearchService SearchService;

    public BlogDataLoader(SearchService SearchService) {
        this.SearchService = SearchService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 프로젝트 실행시 검색어로 개발 관련 블로그 데이터를 가져와 자동으로 저장
        String searchQuery = "tech";

        SearchService.searchAndSaveBlogPosts(searchQuery);
    }
}