package com.develetter.develetter.jobposting.batch;

import com.develetter.develetter.userfilter.entity.JobPostingKeyword;
import com.develetter.develetter.userfilter.entity.UserFilter;
import com.develetter.develetter.userfilter.repository.UserFilterRepository;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.entity.QJobPosting;
import com.develetter.develetter.jobposting.repository.FilteredJobPostingRepository;
import com.develetter.develetter.jobposting.repository.JobPostingRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class JobPostingBatch {

    private final PlatformTransactionManager platformTransactionManager;
    private final JobRepository jobRepository;
    private final JobPostingRepository jobPostingRepository;
    private final FilteredJobPostingRepository filteredJobPostingRepository;
    private final UserFilterRepository userFilterRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManagerFactory emf;

    private static final int chunkSize = 100;

    @Bean
    public JobExecutionListener jobExecutionListener() {

        return new JobExecutionListener() {

            private LocalDateTime startTime;

            @Override
            public void beforeJob(JobExecution jobExecution) {
                startTime = LocalDateTime.now();
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                LocalDateTime endTime = LocalDateTime.now();

                long nanos = ChronoUnit.NANOS.between(startTime, endTime);
                double seconds = nanos / 1_000_000_000.0;

                System.out.println("Job 실행 시간: " + seconds + " 초");
            }
        };
    }

    @Bean
    public Job filterJobPostingsJob(JPAQueryFactory jpaQueryFactory) {
        return new JobBuilder("filterJobPostingsJob", jobRepository)
                .listener(jobExecutionListener())
                .start(filterJobPostingsStep(jpaQueryFactory))
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
            @Value("#{jobParameters['userId']}") Long userId) {

        return jobPosting -> {
            UserFilter userFilter = userFilterRepository.findByUserId(userId)
                    .orElseThrow(EntityNotFoundException::new);
            JobPostingKeyword jobpostingKeywords = userFilter.getJobpostingKeywords();

            List<String> jobNames = parseKeywords(jobpostingKeywords.getJobNames());
            List<String> locationNames = parseKeywords(jobpostingKeywords.getLocationNames());
            List<String> jobTypeNames = parseKeywords(jobpostingKeywords.getJobTypeNames());
            List<String> industryNames = parseKeywords(jobpostingKeywords.getIndustryNames());
            List<String> educationLevelNames = parseKeywords(jobpostingKeywords.getEducationLevelNames());

            // 필터링 조건을 통과하는지 확인
            if (containsIgnoreCase(jobPosting.getJobName(), jobNames) &&
                    containsIgnoreCase(jobPosting.getLocationName(), locationNames) &&
                    containsIgnoreCase(jobPosting.getJobTypeName(), jobTypeNames) &&
                    containsIgnoreCase(jobPosting.getIndustryName(), industryNames) &&
                    containsIgnoreCase(jobPosting.getEducationLevelName(), educationLevelNames)) {

                // 조건을 통과한 경우 JobPosting ID 반환
                return jobPosting.getId(); // ID를 반환
            }
            return null; // 필터 조건을 통과하지 못하면 null 반환
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Long> customItemWriter(@Value("#{jobParameters['userId']}") Long userId) {
        return items -> {
            for (Long jobPostingId : items) {
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
