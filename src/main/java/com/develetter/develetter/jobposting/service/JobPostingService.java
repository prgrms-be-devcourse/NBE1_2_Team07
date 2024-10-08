package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.entity.FilteredJobPosting;

public interface JobPostingService {

    JobSearchResDto searchJobs(int startIdx);

    FilteredJobPosting getFilteredJobPostingByUserId(Long userId);
}
