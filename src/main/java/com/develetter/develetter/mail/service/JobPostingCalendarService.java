package com.develetter.develetter.mail.service;

import com.develetter.develetter.conference.dto.ConferenceResDto;
import com.develetter.develetter.conference.service.ConferenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class JobPostingCalendarService {

    private final ConferenceService conferenceService;

    @Transactional
    public String createJobPostingCalendar() {
        try {
            LocalDate today = LocalDate.now();
            List<ConferenceResDto> conferenceList = conferenceService.getAllConferenceWithDateRange(today, today.plusMonths(1));
            log.info("Success Create Conference Calendar");
            return generateCalendarHtml(conferenceList);
        } catch (Exception e) {
            log.error("Error Create Conference Calendar", e);
        }
        return null;
    }

    // 정적 HTML 생성 코드
    public String generateCalendarHtml(List<ConferenceResDto> conferenceList) {
        StringBuilder htmlContent = new StringBuilder();

        // HTML 구조 작성
        htmlContent.append("<!DOCTYPE html>")
                .append("<html lang=\"ko\">")
                .append("<head>")
                .append("<meta charset=\"UTF-8\">")
                .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("<title>채용 공고 캘린더</title>")
                .append("<style>")
                .append("body {")
                .append("font-family: Arial, sans-serif;")
                .append("margin: 0;")
                .append("padding: 20px;")
                .append("background-color: #f0f0f0;")
                .append("}")
                .append(".calendar-container {")
                .append("max-width: 1000px;")
                .append("margin: 0 auto;")
                .append("background-color: white;")
                .append("box-shadow: 0 0 10px rgba(0,0,0,0.1);")
                .append("border-radius: 10px;")
                .append("overflow: hidden;")
                .append("}")
                .append(".calendar-header {")
                .append("background-color: #4a90e2;")
                .append("color: white;")
                .append("padding: 20px;")
                .append("text-align: center;")
                .append("font-size: 24px;")
                .append("}")
                .append(".calendar-body {")
                .append("padding: 20px;")
                .append("}")
                .append("table {")
                .append("width: 100%;")
                .append("border-collapse: collapse;")
                .append("}")
                .append("th, td {")
                .append("border: 1px solid #ddd;")
                .append("padding: 10px;")
                .append("text-align: left;")
                .append("vertical-align: top;")
                .append("height: 100px;")
                .append("width: 14.28%;")
                .append("}")
                .append("th {")
                .append("background-color: #f2f2f2;")
                .append("font-weight: bold;")
                .append("height: auto;")
                .append("text-align: center;")
                .append("}")
                .append(".past-date {")
                .append("  color: #ccc;")
                .append("}")
                .append(".today {")
                .append("background-color: #fffacd;")
                .append("}")
                .append(".event {")
                .append("background-color: #e6f3ff;")
                .append("margin: 2px 0;")
                .append("padding: 1px 2px;")
                .append("font-size: 11px;")
                .append("line-height: 1.2;")
                .append("}")
                .append(".date {")
                .append("font-weight: bold;")
                .append("margin-bottom: 5px;")
                .append("}")
                .append(".conference-list {")
                .append("  margin-top: 20px;")
                .append("  padding: 20px;")
                .append("  background-color: white;")
                .append("  border-radius: 10px;")
                .append("  box-shadow: 0 0 10px rgba(0,0,0,0.1);")
                .append("}")
                .append(".conference-item {")
                .append("  margin-bottom: 20px;")
                .append("  padding: 15px;")
                .append("  border: 1px solid #ddd;")
                .append("  border-radius: 5px;")
                .append("}")
                .append(".conference-item h3 {")
                .append("  margin-top: 0;")
                .append("  color: #4a90e2;")
                .append("}")
                .append(".conference-item p {")
                .append("  margin: 5px 0;")
                .append("}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<div class=\"calendar-container\">")
                .append("<div class=\"calendar-header\">")
                .append("다가오는 컨퍼런스 일정")
                .append("</div>")
                .append("<div class=\"calendar-body\">")
                .append("<table>")
                .append("<thead><tr>")
                .append("<th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th><th>일</th>")
                .append("</tr></thead>")
                .append("<tbody>");

        // 현재 날짜 설정
        LocalDate today = LocalDate.now();
        today = today.plusDays(2);
        LocalDate endDate = today.plusDays(27); // 4주 후

        // 첫 주의 시작일 조정
        DayOfWeek firstDayOfWeek = today.getDayOfWeek();
        LocalDate startDate = today.minusDays(firstDayOfWeek.getValue() - 1);

        // 4주 동안의 달력 생성
        for (int week = 0; week < 4; week++) {
            htmlContent.append("<tr>");
            for (int day = 0; day < 7; day++) {
                LocalDate currentDate = startDate.plusDays(week * 7 + day);
                String dateClass = "";

                if (currentDate.isBefore(today)) {
                    dateClass = "past-date";
                } else if (currentDate.equals(today)) {
                    dateClass = "today";
                }

                htmlContent.append("<td class=\"").append(dateClass).append("\">")
                        .append("<div class=\"date\">")
                        .append(currentDate.getMonthValue()).append("/")
                        .append(currentDate.getDayOfMonth())
                        .append("</div>");

                // 컨퍼런스 이벤트 추가
                for (ConferenceResDto conference : conferenceList) {
                    if (isDateInRange(currentDate, conference.applyStartDate(), conference.applyEndDate())) {
                        htmlContent.append("<div class=\"event\">")
                                .append(conference.name())
                                .append("</div>");
                    }
                }

                htmlContent.append("</td>");
            }
            htmlContent.append("</tr>");
        }

        htmlContent.append("</tbody>")
                .append("</table>")
                .append("</div>")
                .append("</div>");

        // 컨퍼런스 상세 정보 추가
        htmlContent.append("<div class=\"conference-list\">")
                .append("<h2>채용 공고 상세 정보</h2>");

        for (ConferenceResDto conference : conferenceList) {
            htmlContent.append("<div class=\"conference-item\">")
                    .append("<h3>").append(conference.name()).append(" | ").append(conference.host()).append("</h3>")
                    .append("<p>신청 기간: ").append(conference.applyStartDate()).append(" ~ ").append(conference.applyEndDate()).append("</p>")
                    .append("<p>진행 기간: ").append(conference.startDate()).append(" ~ ").append(conference.endDate()).append("</p>")
                    .append("</div>");
        }

        htmlContent.append("</div>")
                .append("</body>")
                .append("</html>");

        return htmlContent.toString();
    }

    private boolean isDateInRange(LocalDate date, LocalDate start, LocalDate end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}