package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.global.util.DtoUtil;
import com.develetter.develetter.jobposting.dto.JobSearchReqDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingServiceImpl implements JobPostingService {

    private static final int START_INDEX = 0;
    private static final int END_INDEX = 0;

    @Value("${api.saramin.accesskey}")
    private String accessKey;

    @Value("${api.saramin.baseurl}")
    private String baseUrl;

    private final WebClient webClient;
    private final JobPostingRepository jobPostingRepository;


    public JobSearchResDto searchJobs(JobSearchReqDto jobSearchReqDto) {
//        Map<String, Object> params = DtoUtil.toMap(jobSearchReqDto);

//        return webClient.get()
//                .uri(uriBuilder -> {
////                    uriBuilder.path(baseUrl)
//                            .queryParam("access-key", accessKey);
//                    params.forEach((key, value) -> {
//                        Optional.ofNullable(value)
//                                .ifPresent(v -> uriBuilder.queryParam(key, v));
//                    });
//                    return uriBuilder.build();
//                })
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(JobSearchResDto.class)
//                .block(); // 동기 처리

        String testURL = "http://localhost:8080/jobposting/test";
        try {
            JobSearchResDto jobsDto = webClient.get()
                    .uri(testURL)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(JobSearchResDto.class)
                    .block();// 동기 처리

            JobSearchResDto.Jobs jobs = jobsDto.jobs();

            // 여기서 jobs.job()을 호출하여 List<Job>에 접근
            List<JobPosting> jobPostings = jobs.job().stream()
                    .map(JobSearchResDto.Job::toEntity)
                    .collect(Collectors.toList());

            jobPostingRepository.saveAll(jobPostings);  // 여러 엔티티를 한 번에 저장
            log.info("success [searchJobs]");

            return jobsDto;

        } catch (Exception e) {
            log.error("Error occurred during [searchJobs] ", e);
            throw e;
        }
    }
}

