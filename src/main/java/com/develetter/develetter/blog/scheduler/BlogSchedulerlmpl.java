package com.develetter.develetter.blog.scheduler;

import com.develetter.develetter.blog.repository.BlogRepository;
import com.develetter.develetter.blog.service.InterestServicelmpl;
import com.develetter.develetter.blog.service.SearchServicelmpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlogSchedulerlmpl implements BlogScheduler{

    private final SearchServicelmpl searchServicelmpl;
    private final BlogRepository blogRepository;
    private final InterestServicelmpl interestServicelmpl;

    // 매주 월요일 자정에 실행
    @Scheduled(cron = "0 0 0 * * MON")
    //@Scheduled(cron = "0 * * * * *")
    //@Scheduled(cron = "*/10 * * * * *")
//    @Scheduled(fixedRate = 20000)
    @Transactional
    public void fetchAndStoreBlogData() {
        // 블로그 테이블 초기화 (모든 데이터 삭제)
        blogRepository.deleteAll();
        // AUTO_INCREMENT 값 리셋
        blogRepository.resetAutoIncrement();
        // 관심사 목록을 가져와서 각각의 관심사에 대해 블로그 데이터를 저장
        List<String> interests = interestServicelmpl.getInterests();
        interests.forEach(searchServicelmpl::searchAndSaveBlogPosts);  // 각 관심사별로 데이터 저장
        log.info("패치된 Blog 데이터가 저장되었습니다: {}", System.currentTimeMillis());
    }
}
