package com.develetter.develetter.jobposting.controller;

import com.develetter.develetter.global.dto.ApiResponseDto;
import com.develetter.develetter.jobposting.dto.JobSearchReqDto;
import com.develetter.develetter.jobposting.dto.JobSearchResDto;
import com.develetter.develetter.jobposting.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobposting")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;

    @GetMapping("/test")
    public String testSaraminApiCall() {
        return "{\n" +
                "  \"jobs\": {\n" +
                "    \"count\": 2,\n" +
                "    \"start\": 1,\n" +
                "    \"total\": \"7629\",\n" +
                "    \"job\": [\n" +
                "      {\n" +
                "        \"url\": \"http://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=27614114&utm_source=job-search-api&utm_medium=api&utm_campaign=saramin-job-search-api\",\n" +
                "        \"active\": 1,\n" +
                "        \"company\": {\n" +
                "          \"detail\": {\n" +
                "            \"href\":\"http://www.saramin.co.kr/zf_user/company-info/view?csn=1138600917&utm_source=job-search-api&utm_medium=api&utm_campaign=saramin-job-search-api\",\n" +
                "            \"name\": \"(주)사람인\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"position\": {\n" +
                "          \"title\": \"(주)사람인 사무보조·문서작성 경력 채용합니다11212\",\n" +
                "          \"industry\": {\n" +
                "            \"code\": \"301\",\n" +
                "            \"name\": \"솔루션·SI·ERP·CRM\"\n" +
                "          },\n" +
                "          \"location\": {\n" +
                "            \"code\": \"101050,101060,101070\",\n" +
                "            \"name\": \"서울 > 관악구,서울 > 광진구,서울 > 구로구\"\n" +
                "          },\n" +
                "          \"job-type\": {\n" +
                "            \"code\": \"1\",\n" +
                "            \"name\": \"정규직\"\n" +
                "          },\n" +
                "          \"job-mid-code\": {\n" +
                "            \"code\": \"22\",\n" +
                "            \"name\": \"요리·제빵사·영양사\"\n" +
                "          },\n" +
                "          \"job-code\": {\n" +
                "            \"code\": \"2323\",\n" +
                "            \"name\": \"요리·제빵사·영양사\"\n" +
                "          },\n" +
                "          \"experience-level\": {\n" +
                "            \"code\": 2,\n" +
                "            \"min\": 2,\n" +
                "            \"max\": 3,\n" +
                "            \"name\": \"경력 2~3년\"\n" +
                "          },\n" +
                "          \"required-education-level\": {\n" +
                "            \"code\": \"0\",\n" +
                "            \"name\": \"학력무관\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"keyword\": \"SI·시스템통합,Excel·도표,PowerPoint,전산입력·편집\",\n" +
                "        \"salary\": {\n" +
                "          \"code\": \"6\",\n" +
                "          \"name\": \"1,800~2,000만원\"\n" +
                "        },\n" +
                "        \"id\": \"27614114\",\n" +
                "        \"posting-timestamp\": \"1559191564\",\n" +
                "        \"posting-date\": \"2019-05-30T13:46:04+0900\",\n" +
                "        \"modification-timestamp\": \"1559191564\",\n" +
                "        \"opening-timestamp\": \"1559188800\",\n" +
                "        \"expiration-timestamp\": \"1561820399\",\n" +
                "        \"expiration-date\": \"2019-06-29T23:59:59+0900\",\n" +
                "        \"close-type\": {\n" +
                "          \"code\": \"1\",\n" +
                "          \"name\": \"접수마감일\"\n" +
                "        },\n" +
                "        \"read-cnt\": \"0\",\n" +
                "        \"apply-cnt\": \"0\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"url\":\"http://www.saramin.co.kr/zf_user/jobs/relay/view?rec_idx=27614112&utm_source=job-search-api&utm_medium=api&utm_campaign=saramin-job-search-api\",\n" +
                "        \"active\": 1,\n" +
                "        \"company\": {\n" +
                "          \"detail\": {\n" +
                "            \"href\":\"http://www.saramin.co.kr/zf_user/company-info/view?csn=1138600917&utm_source=job-search-api&utm_medium=api&utm_campaign=saramin-job-search-api\",\n" +
                "            \"name\": \"(주)사람인테스트계정04\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"position\": {\n" +
                "          \"title\": \"건축·인테리어·설계 외 2개 부문 담당자 모집 공고123\",\n" +
                "          \"industry\": {\n" +
                "            \"code\": \"1005\",\n" +
                "            \"name\": \"연구소·컨설팅·조사\"\n" +
                "          },\n" +
                "          \"location\": {\n" +
                "            \"code\": \"101070\",\n" +
                "            \"name\": \"서울 > 구로구\"\n" +
                "          },\n" +
                "          \"job-type\": {\n" +
                "            \"code\": \"1\",\n" +
                "            \"name\": \"정규직\"\n" +
                "          },\n" +
                "          \"industry-keyword-code\": \"100501\",\n" +
                "          \"experience-level\": {\n" +
                "            \"code\": 1,\n" +
                "            \"min\": 0,\n" +
                "            \"max\": 0,\n" +
                "            \"name\": \"신입\"\n" +
                "          },\n" +
                "          \"required-education-level\": {\n" +
                "            \"code\": \"0\",\n" +
                "            \"name\": \"학력무관\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"keyword\": \"연구소,전기공사,창호공사,항공사무\",\n" +
                "        \"salary\": {\n" +
                "          \"code\": \"4\",\n" +
                "          \"name\": \"1,400~1,600만원\"\n" +
                "        },\n" +
                "        \"id\": \"27614112\",\n" +
                "        \"posting-timestamp\": \"1559175921\",\n" +
                "        \"posting-date\": \"2019-05-30T09:25:21+0900\",\n" +
                "        \"modification-timestamp\": \"1559175921\",\n" +
                "        \"opening-timestamp\": \"1559174400\",\n" +
                "        \"expiration-timestamp\": \"1561820399\",\n" +
                "        \"expiration-date\": \"2019-06-29T23:59:59+0900\",\n" +
                "        \"close-type\": {\n" +
                "          \"code\": \"1\",\n" +
                "          \"name\": \"접수마감일\"\n" +
                "        },\n" +
                "        \"read-cnt\": \"0\",\n" +
                "        \"apply-cnt\": \"0\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
    }

    @GetMapping("/call")
    public ApiResponseDto<JobSearchResDto> callJobSearchApi(@ModelAttribute JobSearchReqDto jobSearchReqDto) {
        // DTO를 Service로 전달
        JobSearchResDto jobPosting = jobPostingService.searchJobs(jobSearchReqDto);
        return new ApiResponseDto<>(200, "성공", jobPosting);
    }
}