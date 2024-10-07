package com.develetter.develetter.blog.scheduler;

import com.develetter.develetter.blog.Util.BlogUtil;
import com.develetter.develetter.blog.entity.Blog;
import com.develetter.develetter.blog.repository.BlogRepository;
import com.develetter.develetter.blog.service.InterestService;
import com.develetter.develetter.blog.service.SearchService;
import com.develetter.develetter.jobposting.UserFilter;
import com.develetter.develetter.jobposting.UserFilterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlogSchedulerImpl implements BlogScheduler {

    private final SearchService searchService;
    private final BlogRepository blogRepository;
    private final InterestService interestService;
    private final UserFilterRepository userFilterRepository;

    // 매주 월요일 자정에 실행
    //@Scheduled(cron = "0 0 0 * * MON")
    @Scheduled(fixedRate = 2000000)
    @Transactional
    public void fetchAndStoreBlogData() {
        // 블로그 테이블 초기화 (모든 데이터 삭제)
        blogRepository.deleteAll();
        blogRepository.resetAutoIncrement();

        // 관심사 목록을 가져와서 각각의 관심사에 대해 블로그 데이터를 저장
        List<String> interests = interestService.getInterests();
        interests.forEach(searchService::searchAndSaveBlogPosts);
        log.info("패치된 Blog 데이터가 저장되었습니다: {}", System.currentTimeMillis());

        // 모든 사용자 필터 조회
        List<UserFilter> userFilters = userFilterRepository.findAll();
        for (UserFilter userFilter : userFilters) {
            Long userId = userFilter.getUserId();
            List<String> interestsForUser = BlogUtil.getBlogKeywordsAsList(userFilter.getBlogKeywords());

            if (interestsForUser.isEmpty()) {
                log.warn("사용자 {} 의 관심사 리스트가 비어있습니다.", userId);
                return;
            }

            // 사용자 관심사 리스트 중 하나를 랜덤으로 선택
            String randomInterest = interestsForUser.get(new Random().nextInt(interestsForUser.size()));

            // 랜덤으로 선택된 관심사에 맞는 중복되지 않은 블로그를 검색
            Blog blog = interestService.getRandomBlogBySearchQuery(userId, randomInterest);
            if (blog == null) {
                log.warn("사용자 {} 의 관심사 '{}'에 맞는 블로그가 없습니다.", userId, randomInterest); //이 경우에도 추후 프론트쪽 연결해서 처리 필요 (메시지 전달)
                return;
            }

            log.info("사용자 {} 의 관심사 '{}'에 맞는 블로그 id {}을(를) 저장했습니다.", userId, randomInterest, blog.getId());
        }
    }

}