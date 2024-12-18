package com.develetter.develetter.mail.service;

import com.develetter.develetter.conference.dto.ConferenceResDto;
import com.develetter.develetter.conference.service.ConferenceService;
import com.develetter.develetter.jobposting.dto.JobPostingEmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JobPostingCalendarService {

    @Transactional
    public String createJobPostingCalendar(List<JobPostingEmailDto> jobPostingList) {
        try {
            return generateCalendarHtml(jobPostingList);
        } catch (Exception e) {
            log.error("Error Create JobPosting Calendar", e);
        }
        return null;
    }

    public String generateCalendarHtml(List<JobPostingEmailDto> jobPostingList) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<div style='max-width: 950px; margin: 0 auto; font-family: Arial, sans-serif;'>")
                .append("<div style= 'background-color: #004EA2; color: white; padding: 20px; text-align: center; font-size: large; border-radius: 10px 10px 0 0 ;'>")
                .append("<h2>채용 일정</h2></div>")
                .append("<table style='width: 100%; border-collapse: collapse;'>")
                .append("<tr style='background-color: #f2f2f2;'>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>월</th>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>화</th>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>수</th>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>목</th>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>금</th>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>토</th>")
                .append("<th style='padding: 10px; border: 1px solid #ddd; text-align: center;'>일</th>")
                .append("</tr>");

        // 현재 날짜 설정
        LocalDate today = LocalDate.now();
        // 첫 주의 시작일 조정
        DayOfWeek firstDayOfWeek = today.getDayOfWeek();
        LocalDate startDate = today.minusDays(firstDayOfWeek.getValue() - 1);

        // 4주 동안의 달력 생성
        for (int week = 0; week < 4; week++) {
            htmlContent.append("<tr>");
            for (int day = 0; day < 7; day++) {
                LocalDate currentDate = startDate.plusDays(week * 7 + day);
                String cellStyle = "padding: 10px; border: 1px solid #ddd; vertical-align: top; height: 120px; width: 14%;";

                if (currentDate.equals(today)) {
                    cellStyle += " background-color: #fffacd;";
                } else if (currentDate.isBefore(today)) {
                    cellStyle += " color: #ccc;";
                }

                htmlContent.append("<td style='").append(cellStyle).append("'>")
                        .append("<div style='font-weight: bold;'>")
                        .append(currentDate.getMonthValue()).append("/")
                        .append(currentDate.getDayOfMonth())
                        .append("</div>");

                // 채용 공고 이벤트 추가
                for (JobPostingEmailDto jobPosting : jobPostingList) {
                    if (isDateInRange(currentDate, jobPosting.postingDate().toLocalDate(), jobPosting.expirationDate().toLocalDate())) {
                        htmlContent.append("<div style='background-color: #CCDEF0; margin: 2px 0; padding: 1px 2px; font-size: 11px; line-height: 1.2;'>")
                                .append(jobPosting.title()).append(" | ").append(jobPosting.companyName())
                                .append("</div>");
                    }
                }

                htmlContent.append("</td>");
            }
            htmlContent.append("</tr>");
        }

        htmlContent.append("</table>");

        // 채용 공고 상세 정보 추가
        htmlContent.append("<div style='margin-top: 20px;'>")
                .append("<h2 style='color: #004EA2; font-size: medium'>채용 공고 상세 정보</h2>");

        for (JobPostingEmailDto jobPosting : jobPostingList) {
            htmlContent.append("<div style='margin-bottom: 15px; padding: 10px; border: 1px solid #ddd; border-radius: 5px;'>")
                    .append("<h4 style='margin: 0 0 10px 0; color: #004EA2'>")
                    .append(jobPosting.title()).append(" | ").append(jobPosting.companyName()).append("</a></h4>")
                    .append("<p style='margin: 5px 0;'>산업 이름: ").append(jobPosting.industryName()).append("</p>")
                    .append("<p style='margin: 5px 0;'>경력 요건: ").append(jobPosting.experienceName()).append("</p>")
                    .append("<p style='margin: 5px 0;'>직무 형태: ").append(jobPosting.jobTypeName()).append("</p>")
                    .append("<p style='margin: 5px 0;'>근무지: ").append(jobPosting.locationName()).append("</p>")
                    .append("<p style='margin: 5px 0;'>공고 기간: ").append(jobPosting.postingDate().toLocalDate()).append(" ~ ").append(jobPosting.expirationDate().toLocalDate()).append("</p>")
                    .append("<a href='").append(jobPosting.url()).append("' style='color: #004EA2; text-decoration: none;'>&rarr; 자세히 보기</a>")
                    .append("</div>");
        }

        htmlContent.append("</div></div>");

        return htmlContent.toString();
    }

    private boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}