package com.develetter.develetter.jobposting.service;

import com.develetter.develetter.jobposting.dto.JobSearchReqDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;

public interface JobPostingService {

    JobSearchResDto searchJobs(JobSearchReqDto jobSearchReqDto);

}