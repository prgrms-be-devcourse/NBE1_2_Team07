package com.develetter.develetter.mail.service;

import com.develetter.develetter.blog.dto.BlogDto;
import com.develetter.develetter.blog.service.InterestService;
import com.develetter.develetter.jobposting.dto.JobPostingEmailDto;
import com.develetter.develetter.jobposting.entity.JobPosting;
import com.develetter.develetter.jobposting.service.JobPostingService;
import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class AsyncMailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private final MailService mailService;
    private final InterestService blogService;
    private final UserService userService;
    private final JobPostingService jobPostingService;
    private final JobPostingCalendarService jobPostingCalendarService;

    //메일 전송 메서드
    @Async
    public void sendMail(MailResDto mailResDto, String conferenceHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            // user 테이블에서 userId로 email 찾기
            String email = userService.getEmailByUserId(mailResDto.userId());

            //filtered_job_posting 테이블에서 filtered_job_posting_id로 채용공고 리스트 찾기
            List<JobPostingEmailDto> jobPostingList = jobPostingService.getFilteredJobPostingsByUserId(mailResDto.userId());

            //채용공고 리스트로 job_posting Calendar 생성
            String jobPostingHtml = jobPostingCalendarService.createJobPostingCalendar(jobPostingList);

            //filtered_blog 테이블에서 filtered_blog_id로 블로그 데이터 찾기
            BlogDto blog = blogService.getBlogByUserId(mailResDto.userId());

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(getWeekOfMonth(LocalDate.now()) +  " develetter 뉴스레터");
            mimeMessageHelper.setText(setContext(getWeekOfMonth(LocalDate.now()), jobPostingHtml, blog, conferenceHtml), true);
            javaMailSender.send(mimeMessage);
            //메일 발송 체크
            mailService.updateMailSendingCheck(mailResDto.id());
            //메일 DB 삭제
            mailService.updateMailDeleted(mailResDto.id());
            log.info("Send Mail Success");
        } catch (MessagingException e) {
            log.error("Sending Mail Failed", e);
        }
    }

    // 날짜 (ex. 9월 둘째주) 가져오는 메서드
    public String getWeekOfMonth(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            LocalDate lastDayOfLastMonth = localDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
            return getWeekOfMonth(lastDayOfLastMonth);
        }

        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        // 마지막 주차의 경우 마지막 날이 월~수 사이이면 다음달 1주차로 계산
        if (weekOfMonth == lastDayOfMonth.get(weekFields.weekOfMonth()) && lastDayOfMonth.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) < 0) {
            LocalDate firstDayOfNextMonth = lastDayOfMonth.plusDays(1); // 마지막 날 + 1일 => 다음달 1일
            return getWeekOfMonth(firstDayOfNextMonth);
        }

        String[] koreanWeeks = {"첫", "둘", "셋", "넷", "다섯"};
        String weekInKorean = koreanWeeks[weekOfMonth - 1];

        return localDate.getMonthValue() + "월 " + weekInKorean + "째 주";
    }


    // thymeleaf를 통한 mail.html 적용
    public String setContext(String date, String JobPostingHtml, BlogDto blogDto, String conferenceHtml) {
        Context context = new Context();
        context.setVariable("date", date);
        context.setVariable("jobPostingHtml", JobPostingHtml);
        context.setVariable("blog", blogDto);
        context.setVariable("conferenceHtml", conferenceHtml);
        return templateEngine.process("email", context);
    }
}

