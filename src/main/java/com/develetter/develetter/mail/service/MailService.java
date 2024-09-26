package com.develetter.develetter.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@EnableScheduling
public class MailService {
    private final AsyncMailService asyncMailService;

    // 매분 10초마다
    @Scheduled(cron = "10 * * * * *")
    // 월요일 오전 9시마다
    //@Scheduled(cron = "0 0 9 * * MON")
    public void scheduleEmailCron(){
        try {
//        List<Member> activeMembers = memberRepository.findMembersByProfileIsEmailActive(true);
//
//        for (Member member : activeMembers) {
//            String email = member.getEmail();
//            asyncEmailService.sendEmailNotice(email);
//        }

            List<String> mailList = new ArrayList<>();
            mailList.add("dkdudab@naver.com");
            mailList.add("dkdudab@hanmail.net");

            for (String email : mailList) {
                asyncMailService.sendMail(email);
            }

            log.info("All Mails Sent");
        } catch (Exception e) {
            log.error("Scheduled Mail Sent Error");
            throw new RuntimeException(e);
        }

    }
}
