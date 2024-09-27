package com.develetter.develetter.jobposting.controller;

import com.develetter.develetter.global.dto.ApiResponseDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping("/testCall")
    public ApiResponseDto<JobSearchResDto> callJobSearchApi() {
        // DTO를 Service로 전달
        JobSearchResDto jobPosting = jobPostingService.searchJobs();
        return new ApiResponseDto<>(200, "성공", jobPosting);
    }
}