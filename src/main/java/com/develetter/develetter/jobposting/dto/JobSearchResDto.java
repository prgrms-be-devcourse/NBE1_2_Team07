package com.develetter.develetter.jobposting.dto;

import com.develetter.develetter.jobposting.entity.JobPosting;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public record JobSearchResDto(
        Jobs jobs
) {
    public record Jobs(
            int count,    // job 엘리먼트 개수
            int start,    // 검색 결과의 페이지 번호
            int total,    // 총 검색 결과 수
            List<Job> job // 채용공고 엘리먼트 목록
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public record Job(
            String url,             // 채용공고 표준 URL
            int active,             // 공고 진행 여부 (1: 진행중, 0: 마감)
            Company company,        // 회사 정보
            Position position,      // 직무 정보
            String keyword,         // 키워드
            Salary salary,          // 연봉 정보
            long id,                // 공고 번호
            long postingTimestamp,  // 게시일 Unix timestamp
            String postingDate,     // 게시일 (선택: 날짜/시간 형식)
            long modificationTimestamp, // 수정일 Unix timestamp
            long openingTimestamp,  // 접수 시작일 Unix timestamp
            long expirationTimestamp, // 마감일 Unix timestamp
            String expirationDate,  // 마감일 (선택: 날짜/시간 형식)
            CloseType closeType,    // 마감일 형식
            Integer readCnt,        // 조회수 (선택)
            Integer applyCnt        // 지원자수 (선택)
    ) {
        // 정적 팩토리 메서드
        public static JobPosting toEntity(Job job) {

            return JobPosting.builder()
                    .url(job.url)
                    .active(job.active == 1)  // 1이면 true, 0이면 false
                    .companyName(Optional.ofNullable(job.company()).map(JobSearchResDto.Company::detail).map(JobSearchResDto.Detail::name).orElse(null))
                    .companyUrl(Optional.ofNullable(job.company()).map(JobSearchResDto.Company::detail).map(JobSearchResDto.Detail::href).orElse(null))
                    .title(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::title).orElse(null))
                    .industryCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::industry).map(JobSearchResDto.Industry::code).orElse(null))
                    .industryName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::industry).map(JobSearchResDto.Industry::name).orElse(null))
                    .locationCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::location).map(JobSearchResDto.Location::code).orElse(null))
                    .locationName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::location).map(JobSearchResDto.Location::name).orElse(null))
                    .jobTypeCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::jobType).map(JobSearchResDto.JobType::code).orElse(null))
                    .jobTypeName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::jobType).map(JobSearchResDto.JobType::name).orElse(null))
                    .jobMidCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::jobMidCode).map(JobSearchResDto.JobMidCode::code).orElse(null))
                    .jobMidName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::jobMidCode).map(JobSearchResDto.JobMidCode::name).orElse(null))
                    .jobCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::jobCode).map(JobSearchResDto.JobCode::code).orElse(null))
                    .jobName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::jobCode).map(JobSearchResDto.JobCode::name).orElse(null))
                    .experienceCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::experienceLevel).map(JobSearchResDto.ExperienceLevel::code).orElse(null))
                    .experienceMin(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::experienceLevel).map(JobSearchResDto.ExperienceLevel::min).orElse(null))
                    .experienceMax(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::experienceLevel).map(JobSearchResDto.ExperienceLevel::max).orElse(null))
                    .experienceName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::experienceLevel).map(JobSearchResDto.ExperienceLevel::name).orElse(null))
                    .educationLevelCode(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::requiredEducationLevel).map(JobSearchResDto.RequiredEducationLevel::code).orElse(null))
                    .educationLevelName(Optional.ofNullable(job.position()).map(JobSearchResDto.Position::requiredEducationLevel).map(JobSearchResDto.RequiredEducationLevel::name).orElse(null))
                    .keyword(job.keyword)
                    .salaryCode(Optional.ofNullable(job.salary()).map(JobSearchResDto.Salary::code).orElse(null))
                    .salaryName(Optional.ofNullable(job.salary()).map(JobSearchResDto.Salary::name).orElse(null))
                    .postingTimestamp(job.postingTimestamp)
                    .postingDate(OffsetDateTime.parse(job.postingDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).toLocalDateTime())
                    .modificationTimestamp(job.modificationTimestamp)
                    .openingTimestamp(job.openingTimestamp)
                    .expirationTimestamp(job.expirationTimestamp)
                    .expirationDate(OffsetDateTime.parse(job.expirationDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).toLocalDateTime())
                    .closeTypeCode(Optional.ofNullable(job.closeType()).map(JobSearchResDto.CloseType::code).orElse(null))
                    .closeTypeName(Optional.ofNullable(job.closeType()).map(JobSearchResDto.CloseType::name).orElse(null))
                    .readCount(job.readCnt != null ? job.readCnt : 0)
                    .applyCount(job.applyCnt != null ? job.applyCnt : 0)
                    .build();
        }
    }

    public record Company(
            Detail detail // 기업 세부 정보
    ) {
    }

    public record Detail(
            String href,  // 기업 정보 페이지
            String name    // 기업명
    ) {
    }

    @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
    public record Position(
            String title,             // 공고 제목
            Industry industry,        // 업종 정보
            Location location,        // 지역 정보
            JobType jobType,          // job-type 필드 자동 매핑
            JobMidCode jobMidCode,    // job-mid-code 필드 자동 매핑
            JobCode jobCode,          // job-code 필드 자동 매핑
            ExperienceLevel experienceLevel,  // experience-level 필드 자동 매핑
            RequiredEducationLevel requiredEducationLevel  // required-education-level 필드 자동 매핑
    ) {
    }

    public record Industry(
            String code,  // 업종 코드
            String name   // 업종명
    ) {
    }

    public record Location(
            String code,  // 지역 코드
            String name   // 지역명
    ) {
    }

    public record JobType(
            String code,  // 근무형태 코드
            String name   // 근무형태 값
    ) {
    }

    public record JobMidCode(
            String code,  // 상위 직무 코드
            String name   // 상위 직무명
    ) {
    }

    public record JobCode(
            String code,  // 직무 코드
            String name   // 직무명
    ) {
    }

    public record ExperienceLevel(
            int code,  // 경력 코드 (1: 신입, 2: 경력, 3: 신입/경력, 0: 경력무관)
            int min,   // 경력 최소 값
            int max,   // 경력 최대 값
            String name // 경력 값
    ) {
    }

    public record RequiredEducationLevel(
            String code,  // 학력 코드
            String name   // 학력 값
    ) {
    }

    public record CloseType(
            String code,  // 마감일 코드 (1: 접수 마감일, 2: 채용시, 3: 상시, 4: 수시)
            String name   // 마감일 값
    ) {
    }

    public record Salary(
            String code,  // 연봉 코드
            String name   // 연봉 값
    ) {
    }


}
