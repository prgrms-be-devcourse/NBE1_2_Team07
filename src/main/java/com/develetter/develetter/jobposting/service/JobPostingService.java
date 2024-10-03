package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;
import com.develetter.develetter.jobposting.entity.JobPosting;

import java.util.List;

public interface JobPostingService {

    JobSearchResDto searchJobs(int startIdx);

    void filterJobPostingByKeywords(Long userId);

    void saveFilteredJobPostings(Long userId, List<Long> jobPostings);


}
