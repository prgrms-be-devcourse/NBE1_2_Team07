package com.develetter.develetter.jobposting.dto;

public record JobSearchReqDto(
        String keywords,
        Integer bbsGb,
        String stock,
        String sr,
        String locCd,
        String locMcd,
        String locBcd,
        String indCd,
        String jobMidCd,
        String jobCd,
        String jobType,
        String eduLv,
        String fields,
        String published,
        String publishedMin,
        String publishedMax,
        String updated,
        String updatedMin,
        String updatedMax,
        String deadline,
        Integer start,
        Integer count,
        String sort
) {
    
}
