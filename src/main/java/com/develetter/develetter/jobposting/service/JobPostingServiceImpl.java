package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.global.util.DtoUtil;
import com.develetter.develetter.jobposting.converter.Converter;
import com.develetter.develetter.jobposting.dto.JobSearchParams;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.exception.JobSearchException;
import com.develetter.develetter.jobposting.repository.JobPostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingServiceImpl implements JobPostingService {

    @Value("${api.saramin.accesskey}")
    private String accessKey;

    @Value("${api.saramin.baseurl}")
    private String baseUrl;

    private final WebClient webClient;
    private final JobPostingRepository jobPostingRepository;

    @Override
    public JobSearchResDto searchJobs(int startPage) {
        try {
            Map<String, Object> params = DtoUtil.toMap(JobSearchParams.defaultParams(startPage));

            JobSearchResDto jobSearchResDto = webClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder
                                .scheme("https")  // 스키마 설정
                                .host(baseUrl)  // 호스트 설정
                                .path("/job-search")  // 경로 설정
                                .queryParam("access-key", accessKey);  // 쿼리 파라미터 추가
                        params.forEach((key, value) -> {
                            Optional.ofNullable(value)
                                    .ifPresent(v -> uriBuilder.queryParam(key, v));
                        });
                        return uriBuilder.build();
                    })
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(JobSearchResDto.class)
                    .block();// 동기 처리

            // 사람인 API Res check
            if (jobSearchResDto == null || jobSearchResDto.jobs().count() == 0 || jobSearchResDto.jobs().job() == null) {
                log.error("사람인 채용정보 API 응답이 올바르지 않습니다: 0개의 채용공고를 응답받았습니다.");
                // 필요시 사용자 정의 예외를 던질 수 있음
                throw new JobSearchException("사람인 API에서 0개의 채용공고 응답이 왔습니다.");
            }

            List<JobPosting> jobPostings = jobSearchResDto.jobs().job().stream()
                    .map(Converter::toEntity)
                    .collect(Collectors.toList());

            jobPostingRepository.saveAll(jobPostings);  // 여러 엔티티를 한 번에 저장
            log.info("success [searchJobs]");

            return jobSearchResDto;

        } catch (Exception e) {
            log.error("Error occurred during [searchJobs] ", e);
            throw e;
        }
    }
}