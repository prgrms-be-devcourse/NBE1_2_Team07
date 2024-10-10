package com.develetter.develetter.jobposting.batch;

import com.develetter.develetter.userfilter.entity.JobPostingKeyword;
import com.develetter.develetter.userfilter.entity.UserFilter;
import com.develetter.develetter.userfilter.repository.UserFilterRepository;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.entity.QJobPosting;
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobPostingBatch {

    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final FilteredJobPostingRepository filteredJobPostingRepository;
    private final UserFilterRepository userFilterRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManagerFactory emf;

    private static final int chunkSize = 100;

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                System.out.println("Job 시작: " + jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    System.out.println("Job 완료: " + jobExecution.getJobInstance().getJobName());
                } else {
                    System.err.println("Job 실패: " + jobExecution.getAllFailureExceptions());
                }
            }
        };
    }


    @Bean
    public Job filterJobPostingsJob(JPAQueryFactory jpaQueryFactory) {
        return new JobBuilder("filterJobPostingsJob", jobRepository)
                .listener(jobExecutionListener())
                .start(filterJobPostingsStep(jpaQueryFactory))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step filterJobPostingsStep(JPAQueryFactory jpaQueryFactory) {
        return new StepBuilder("filterJobPostingsStep", jobRepository)
                .<JobPosting, Long>chunk(chunkSize, platformTransactionManager)
                .reader(reader())
                .processor(itemProcessor(null))
                .writer(customItemWriter(null)) // 사용자 정의 writer 사용
                .build();
    }


//    @Bean
//    public RepositoryItemReader<JobPosting> reader() {
//        return new RepositoryItemReaderBuilder<JobPosting>()
//                .name("jobPostingItemReader")
//                .pageSize(chunkSize)
//                .repository(jobPostingRepository)
//                .methodName("findAll") // 모든 JobPosting을 조회
//                .sorts(Map.of("id", Sort.Direction.ASC))
//                .build();
//    }

    @Bean
    public QuerydslPagingItemReader<JobPosting> reader() {
        return new QuerydslPagingItemReader<>(emf, chunkSize, queryFactory -> queryFactory
                .selectFrom(QJobPosting.jobPosting));
    }

    @Bean
    @StepScope
    public ItemProcessor<JobPosting, Long> itemProcessor(
            @Value("#{jobParameters['userIds']}") String userIds) {

        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        // 모든 UserFilter를 메모리에 로드
        List<UserFilter> userFilters = userFilterRepository.findAll();
        Map<Long, JobPostingKeyword> userFilterMap = userFilters.stream()
                .collect(Collectors.toMap(UserFilter::getUserId, UserFilter::getJobpostingKeywords));

        return jobPosting -> {
            for (Long userId : userIdList) {
                JobPostingKeyword jobpostingKeywords = userFilterMap.get(userId);

                // 필터링 조건을 통과하는지 확인
                if (jobpostingKeywords != null &&
                        containsIgnoreCase(jobPosting.getJobName(), parseKeywords(jobpostingKeywords.getJobNames())) &&
                        containsIgnoreCase(jobPosting.getLocationName(), parseKeywords(jobpostingKeywords.getLocationNames())) &&
                        containsIgnoreCase(jobPosting.getJobTypeName(), parseKeywords(jobpostingKeywords.getJobTypeNames())) &&
                        containsIgnoreCase(jobPosting.getIndustryName(), parseKeywords(jobpostingKeywords.getIndustryNames())) &&
                        containsIgnoreCase(jobPosting.getEducationLevelName(), parseKeywords(jobpostingKeywords.getEducationLevelNames()))) {

                    // 조건을 통과한 경우 JobPosting ID 반환
                    return jobPosting.getId(); // ID를 반환
                }
            }
            return null; // 필터 조건을 통과하지 못하면 null 반환
        };
    }


    @Bean
    @StepScope
    public ItemWriter<Long> customItemWriter(@Value("#{jobParameters['userIds']}") String userIds) {
        List<Long> userIdList = Arrays.stream(userIds.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());

        return items -> {
            for (Long jobPostingId : items) {
                for (Long userId : userIdList) {
                    // 기존의 FilteredJobPosting을 찾거나 새로 생성
                    FilteredJobPosting filteredJobPosting = filteredJobPostingRepository.findByUserId(userId)
                            .orElse(FilteredJobPosting.builder()
                                    .userId(userId)
                                    .jobPostings("") // 초기값 설정
                                    .build());

                    // 새 JobPosting ID를 추가
                    filteredJobPosting.addJobPosting(jobPostingId);

                    // 업데이트된 필터링된 잡 포스팅 저장
                    filteredJobPostingRepository.save(filteredJobPosting);
                }
            }
        };
    }


    private List<String> parseKeywords(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return List.of(); // 빈 리스트 반환
        }
        return List.of(keywords.split(","))
                .stream()
                .map(String::trim)
                .filter(keyword -> !keyword.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    private boolean containsIgnoreCase(String fieldValue, List<String> keywords) {
        if (fieldValue == null) {
            return false;
        }
        return keywords.stream().anyMatch(keyword -> fieldValue.toLowerCase().contains(keyword.toLowerCase()));
    }
}
