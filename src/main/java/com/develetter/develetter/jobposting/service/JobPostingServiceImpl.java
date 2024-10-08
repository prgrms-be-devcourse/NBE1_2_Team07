package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.global.util.DtoUtil;
import com.develetter.develetter.jobposting.JobPostingKeyword;
import com.develetter.develetter.jobposting.UserFilter;
import com.develetter.develetter.jobposting.UserFilterRepository;
import com.develetter.develetter.jobposting.converter.Converter;
import com.develetter.develetter.jobposting.dto.JobSearchParams;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.exception.JobSearchException;
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository;
import com.develetter.develetter.jobposting.repository.JobPostingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
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

    // TODO 유저필터 레포지토리를 주입받지않고, 서비스를 주입받는게 좋지 않을까?
    private final UserFilterRepository userFilterRepository;

    private final FilteredJobPostingRepository filteredJobPostingRepository;

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
            log.info("success [searchJobs] page: {}", startPage);

            return jobSearchResDto;

        } catch (Exception e) {
            log.error("Error occurred during [searchJobs] ", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void filterJobPostingByKeywords(Long userId) {
        UserFilter userFilter = userFilterRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
        JobPostingKeyword jobpostingKeywords = userFilter.getJobpostingKeywords();

        // 각각의 키워드를 ','로 구분하고 공백을 제거하여 리스트로 분리
        List<String> jobNames = parseKeywords(jobpostingKeywords.getJobNames());
        List<String> locationNames = parseKeywords(jobpostingKeywords.getLocationNames());
        List<String> jobTypeNames = parseKeywords(jobpostingKeywords.getJobTypeNames());
        List<String> industryNames = parseKeywords(jobpostingKeywords.getIndustryNames());
        List<String> educationLevelNames = parseKeywords(jobpostingKeywords.getEducationLevelNames());
        // JobPosting 필터링 로직
        List<Long> jobPostingIds = jobPostingRepository.findAll().stream()
                .filter(jobPosting ->
                        // 헬퍼 메서드를 활용하여 중복 코드 제거
                        containsIgnoreCase(jobPosting.getJobName(), jobNames) &&
                                containsIgnoreCase(jobPosting.getLocationName(), locationNames) &&
                                containsIgnoreCase(jobPosting.getJobTypeName(), jobTypeNames) &&
                                containsIgnoreCase(jobPosting.getIndustryName(), industryNames) &&
                                containsIgnoreCase(jobPosting.getEducationLevelName(), educationLevelNames)
                )
                .map(JobPosting::getId)
                .toList();

        saveFilteredJobPostings(userId, jobPostingIds);
    }

    // TODO: 아래 헬퍼 메서드 2개의 위치를 옮겨야한다
    // 문자열 비교 헬퍼 메서드
    private boolean containsIgnoreCase(String fieldValue, List<String> keywords) {
        // fieldValue가 null이면 false 반환
        if (fieldValue == null) {
            return false;
        }
        // 각 키워드에 대해 fieldValue가 포함되는지 체크
        return keywords.stream().anyMatch(keyword -> fieldValue.toLowerCase().contains(keyword.toLowerCase()));
    }

    // 유저 입력값 처리 헬퍼 메서드 (null 체크, trim, 소문자화)
    private List<String> parseKeywords(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return List.of(); // null 또는 빈 문자열일 경우 빈 리스트 반환
        }
        return Arrays.stream(keywords.split(","))
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty()) // 빈 문자열 필터링
                .map(String::toLowerCase) // 소문자화
                .toList();
    }

    @Override
    public void saveFilteredJobPostings(Long userId, List<Long> jobPostings) {
        String jobPostingList = jobPostings.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        FilteredJobPosting filteredJobPosting = FilteredJobPosting.builder()
                .userId(userId)
                .jobPostings(jobPostingList)
                .build();

        filteredJobPostingRepository.save(filteredJobPosting);
    }
}