package com.develetter.develetter.jobposting.scheduler;

import com.develetter.develetter.jobposting.dto.JobSearchReqDto;
import com.develetter.develetter.jobposting.repository.JobPostingRepository;
import com.develetter.develetter.jobposting.service.JobPostingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JobPostingSchedulerImpl implements JobPostingScheduler{

    @Value("${api.saramin.baseurl}")
    private String apiURL;

    private final JobPostingService jobPostingService;

//    private final

    @Override
//    @Scheduled(cron = "0 0 0 * * *") // 매일 00:00시 정각에 실행
    @Scheduled(fixedRate = 5000) // 5초마다 호출 테스트
    @Transactional
    public void fetchJobPostings() {

        JobSearchReqDto emptyDto = new JobSearchReqDto(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);
        jobPostingService.searchJobs(emptyDto);    }
//
//    @Scheduled(cron = "0 0 14 * * ?")
//    @Transactional
//    public void updateOrdersToShippingStarted() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime today2pm = now.withHour(14).withMinute(0).withSecond(0).withNano(0);
//        LocalDateTime yesterday2pm = today2pm.minusDays(1);
//
//        int updatedCount = orderRepository.updateOrderStatusInDateRange(
//                OrderStatus.SHIPPING_STARTED, OrderStatus.ORDER_COMPLETED, yesterday2pm, today2pm, now
//        );
//
//    }
}
