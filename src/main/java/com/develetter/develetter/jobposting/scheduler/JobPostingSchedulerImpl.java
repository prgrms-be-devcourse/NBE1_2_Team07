package com.develetter.develetter.jobposting.scheduler;

import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class JobPostingSchedulerImpl implements JobPostingScheduler {

    @Value("${api.saramin.baseurl}")
    private String apiURL;

    private final JobPostingService jobPostingService;

    @Override
    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00시 정각에 실행
    @Transactional
    public void fetchJobPostings() {

        int startPage = 0;

        String twoWeeksAgo = LocalDateTime.now()
                .minusDays(14)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        while (true) {
            JobSearchResDto jobSearchResDto = jobPostingService.searchJobs(startPage++, twoWeeksAgo);

            if (jobSearchResDto.jobs().count() < 100) break;
        }
    }

}
