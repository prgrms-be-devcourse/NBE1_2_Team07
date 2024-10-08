package com.develetter.develetter.jobposting.repository;

import com.develetter.develetter.jobposting.entity.JobPosting;

import java.util.List;

public interface DslJobPostingRepository {

    public List<JobPosting> findByKeywords(List<String> keywords);
}
