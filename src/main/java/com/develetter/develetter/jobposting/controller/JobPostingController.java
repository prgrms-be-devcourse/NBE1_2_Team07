package com.develetter.develetter.jobposting.controller;

import com.develetter.develetter.global.dto.ApiResponseDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.scheduler.JobPostingSchedulerImpl;
import com.develetter.develetter.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
@Slf4j
public class JobPostingController {

    private final JobPostingService jobPostingService;
    private final JobPostingSchedulerImpl jobPostingSchedulerImpl;

    @GetMapping("/testCall")
    public ApiResponseDto<JobSearchResDto> callJobSearchApi() {
        jobPostingSchedulerImpl.fetchJobPostings();

        return new ApiResponseDto<>(200, "성공");
    }
}