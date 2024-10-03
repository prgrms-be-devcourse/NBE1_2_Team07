package com.develetter.develetter.jobposting.repository;

import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.entity.QJobPosting;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DslJobPostingRepositoryImpl implements DslJobPostingRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<JobPosting> findByKeywords(List<String> keywords) {
        QJobPosting jobPosting = QJobPosting.jobPosting;

        return queryFactory
                .selectFrom(jobPosting)
                .where(hasKeywords(jobPosting, keywords))
                .fetch();
    }

    private BooleanExpression hasKeywords(QJobPosting jobPosting, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return null; // 키워드가 없는 경우 필터링하지 않음
        }

        BooleanExpression predicate = null;
        for (String keyword : keywords) {
            BooleanExpression keywordCondition = jobPosting.jobName.containsIgnoreCase(keyword);
            predicate = (predicate == null) ? keywordCondition : predicate.or(keywordCondition);
        }

        return predicate;
    }
}
