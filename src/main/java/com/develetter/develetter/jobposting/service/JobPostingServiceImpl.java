package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.global.util.DtoUtil;
import com.develetter.develetter.jobposting.dto.JobSearchReqDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

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


    public JobSearchResDto searchJobs(JobSearchReqDto jobSearchReqDto) {
        Map<String, Object> params = DtoUtil.toMap(jobSearchReqDto);


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

        String testURL = "localhost:8080/test";

        return webClient.get()
                .uri(testURL)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JobSearchResDto.class)
                .block(); // 동기 처리
    }
}

