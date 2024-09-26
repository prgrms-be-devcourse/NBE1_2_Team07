package com.develetter.develetter.mail.service;

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
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
public class AsyncMailService {
    // TODO: setQueueCapacity로 비동기로 처리 가능한 이메일 제한 필요
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    //메일 전송 메서드
    @Async
    public void sendMail(String email) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(gettWeekOfMonth(LocalDate.now()) +  " develetter 소식");
            mimeMessageHelper.setText(setContext(gettWeekOfMonth(LocalDate.now())), true);
            javaMailSender.send(mimeMessage);
            log.info("Sending Mail Success");
        } catch (MessagingException e) {
            log.error("Sending Mail Failed");
            throw new RuntimeException(e);
        }
    }

    // 날짜 가져오는 메서드
    public String gettWeekOfMonth(LocalDate localDate) {
        // 한 주의 시작은 월요일이고, 첫 주에 4일이 포함되어있어야 첫 주 취급 (목/금/토/일)
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        int weekOfMonth = localDate.get(weekFields.weekOfMonth());

        // 첫 주에 해당하지 않는 주의 경우 전 달 마지막 주차로 계산
        if (weekOfMonth == 0) {
            LocalDate lastDayOfLastMonth = localDate.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
            return gettWeekOfMonth(lastDayOfLastMonth);
        }

        LocalDate lastDayOfMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        // 마지막 주차의 경우 마지막 날이 월~수 사이이면 다음달 1주차로 계산
        if (weekOfMonth == lastDayOfMonth.get(weekFields.weekOfMonth()) && lastDayOfMonth.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) < 0) {
            LocalDate firstDayOfNextMonth = lastDayOfMonth.plusDays(1); // 마지막 날 + 1일 => 다음달 1일
            return gettWeekOfMonth(firstDayOfNextMonth);
        }

        String[] koreanWeeks = {"첫", "둘", "셋", "넷", "다섯"};
        String weekInKorean = koreanWeeks[weekOfMonth - 1];

        return localDate.getMonthValue() + "월 " + weekInKorean + "째 주";
    }


    // thymeleaf를 통한 html 적용
    public String setContext(String date) {
        Context context = new Context();
        context.setVariable("date", date);
        return templateEngine.process("email", context);
    }
}

