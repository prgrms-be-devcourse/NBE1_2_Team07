package com.develetter.develetter.mail.scheduler;

import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.mail.service.AsyncMailService;
import com.develetter.develetter.mail.service.ConferenceCalendarService;
import com.develetter.develetter.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MailSchedulerImpl implements MailScheduler {
    private final ConferenceCalendarService conferenceCalendarService;
    private final AsyncMailService asyncMailService;
    private final MailService mailService;

    @Override
    // 월요일 오전 8시 55분마다
    @Scheduled(cron = "0 50 8 * * MON")
    public void saveMails() {
        try {
            //메일 내용 저장
           mailService.createMails();

        } catch (Exception e) {
            log.error("Save Mails failed");
        }
    }

    @Override
    // 매분 10초마다
    //@Scheduled(cron = "10 * * * * *")
    // 월요일 오전 9시마다
    @Scheduled(cron = "0 0 9 * * MON")
    public void sendingMails() {

        //conference Calendar 생성
        String conferenceHtml = conferenceCalendarService.createConferenceCalendar();

        try {
            //메일 정보 가져오기
            List<MailResDto> mailList = mailService.getAllMails();

            for (MailResDto mailResDto : mailList) {
                asyncMailService.sendMail(mailResDto, conferenceHtml);
            }

        } catch (Exception e) {
            log.error("Scheduled Mail Sent Error", e);
            //미발송 메일 재전송
            if (mailService.getFailedMails() != null) {
                asyncMailService.sendingFailedMails(conferenceHtml);
                log.info("Failed Mail Sent Success");
            }
        }
    }
}
