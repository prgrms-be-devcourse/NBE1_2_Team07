package com.develetter.develetter.jobposting.scheduler;

import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class JobPostingSchedulerImpl implements JobPostingScheduler {

    @Value("${api.saramin.baseurl}")
    private String apiURL;

    private final JobPostingService jobPostingService;

    @Override
//    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00시 정각에 실행
//    @Scheduled(fixedRate = 5000) // 5초마다 호출 테스트
    @Transactional
    public void fetchJobPostings() {

        int startPage = 0;

        while (true) {

            JobSearchResDto jobSearchResDto = jobPostingService.searchJobs(startPage++);

            if (jobSearchResDto.jobs().count() < 100) break;

        }
    }

}
