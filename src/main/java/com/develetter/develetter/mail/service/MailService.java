package com.develetter.develetter.mail.service;
import com.develetter.develetter.mail.dto.MailResDto;

import java.util.List;

public interface MailService {

    List<MailResDto> getAllMails();

    void createMails();

    void updateMailSendingCheck(Long id);
}
