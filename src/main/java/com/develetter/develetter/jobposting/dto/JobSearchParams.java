package com.develetter.develetter.jobposting.dto;

public record JobSearchParams(
        String keywords,
        Integer bbs_gb,
        String stock,
        String sr,
        String loc_cd,
        String loc_mcd,
        String loc_bcd,
        String ind_cd,
        String job_mid_cd,
        String job_cd,
        String job_type,
        String edu_lv,
        String fields,
        String published,
        String published_min,
        String published_max,
        String updated,
        String updated_min,
        String updated_max,
        String deadline,
        Integer start,
        Integer count,
        String sort
) {
    public static JobSearchParams defaultParams(int start) {
        return new JobSearchParams(
                null, null, null, null, null, null, null, null,
                "2", null, null, null, null, null, null, null,
                null, null, null, null, start, 100, null);
    }
}
