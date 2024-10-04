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
public class MailService {
    private final AsyncMailService asyncMailService;
    //private final UserRepository userRepository;

    // 매분 10초마다
//    @Scheduled(cron = "10 * * * * *")
    // 월요일 오전 9시마다
    //@Scheduled(cron = "0 0 9 * * MON")
    public void scheduleEmailCron(){
        try {
          // DB 연결 시 코드
//        List<Users> UserList = userRepository.findAll();
//
//        for (Users user : UserList) {
//            String email = user.getEmail();
          ////////////////////////////////////////
          // DB 연결 전 코드
            //String email1 = "dkdudab@hanmail.net";
            String email2 = "dkdudab@naver.com";
            List<String> emails = new ArrayList<>();
            //emails.add(email1);
            emails.add(email2);
            for (String email : emails) {
            /////////////////////////////////////////
                asyncMailService.sendMail(email);
            }
            log.info("All Mails Sent");
        } catch (Exception e) {
            log.error("Scheduled Mail Sent Error");
            throw new RuntimeException(e);
        }

    }
}
