package com.develetter.develetter.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailService {
    private final AsyncMailService asyncMailService;
    private final CalendarService calendarService;

//    @Transactional
//    // 매분 5초마다
//    @Scheduled(cron = "5 * * * * *")
//    // 월요일 오전 8시마다
//    //@Scheduled(cron = "0 0 8 * * MON")
//    public void scheduleSaveEmail() {
//        try {
//        } catch (Exception e) {
//
//        }
//    }

    @Transactional
    // 매분 10초마다
    @Scheduled(cron = "10 * * * * *")
    // 월요일 오전 9시마다
    //@Scheduled(cron = "0 0 9 * * MON")
    public void scheduleEmailCron(){
        try {
            //conference Calendar 생성
            String conferenceHtml = calendarService.createConferenceCalendar();

            String email1 = "dkdudab@hanmail.net";
            String email2 = "dkdudab@naver.com";
            List<String> emails = new ArrayList<>();
            emails.add(email1);
            emails.add(email2);
            for (String email : emails) {
            /////////////////////////////////////////
                asyncMailService.sendMail(email, conferenceHtml);
            }

        } catch (Exception e) {
            log.error("Scheduled Mail Sent Error");
        }
    }

}
