package com.develetter.develetter.blog.Scheduler;

import com.develetter.develetter.blog.service.SearchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BlogScheduler {

    private final SearchService searchService;

    public BlogScheduler(SearchService searchService) {
        this.searchService = searchService;
    }

    // 매주 월요일 자정에 실행
    @Scheduled(cron = "0 0 0 * * MON")
    public void fetchAndStoreBlogData() {
        String searchQuery = "tech";  // 주제 >> 추후에 사용자가 선택한 주제를 기반으로 검색하도록 변경해야함

        // API 호출 및 데이터 저장
        searchService.searchAndSaveBlogPosts(searchQuery);

        System.out.println("패치된 Blog 데이터가 저장되었습니다: " + System.currentTimeMillis());
    }
}
