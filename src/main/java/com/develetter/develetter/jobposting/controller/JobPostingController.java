package com.develetter.develetter.jobposting.controller;

import com.develetter.develetter.global.dto.ApiResponseDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.scheduler.JobPostingSchedulerImpl;
import com.develetter.develetter.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final JobPostingSchedulerImpl jobPostingSchedulerImpl;
    private final JobOperator jobOperator;

    @GetMapping("/testCall")
    public ApiResponseDto<JobSearchResDto> callJobSearchApi() {
        jobPostingSchedulerImpl.fetchJobPostings();

        return new ApiResponseDto<>(200, "성공");
    }

    @GetMapping("/testFilter")
    public ApiResponseDto<Void> callTestFilter() {
        jobPostingService.filterJobPostingByKeywords(1L);

        return new ApiResponseDto<>(200, "성공");
    }

//    @GetMapping("/startJob")
//    public String startJob() throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
//        String jobParameters = "time=" + System.currentTimeMillis();
//        jobOperator.start("jobFilteringJob", jobParameters);
//        return "Job started";
//    }
}