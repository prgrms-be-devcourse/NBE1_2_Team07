package com.develetter.develetter.mail.service;

import com.develetter.develetter.mail.converter.Converter;
import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.mail.entity.Mail;
import com.develetter.develetter.mail.repository.MailRepository;
import com.develetter.develetter.user.global.entity.UserEntity;
import com.develetter.develetter.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

    private final MailRepository mailRepository;
    private final UserService userService;

    @Override
    public List<MailResDto> getAllMails() {
        return mailRepository.findByDeletedIsFalse()
                .stream().map(Converter::toDto)
                .toList();
    }

    @Override
    public List<MailResDto> getFailedMails() {
        Optional<List<Mail>> failedMailList = mailRepository.findBySendingCheckIsFalse();
        return failedMailList.map(mail -> mail
                .stream().map(Converter::toDto)
                .toList()).orElse(null);
    }


    @Transactional
    @Override
    public void createMails() {
        //User 테이블의 모든 id 가져오기
        List<UserEntity> userEntityList = userService.getAllUsers();
        System.out.println(userEntityList);

        //Mail 테이블에 저장
        for (UserEntity user : userEntityList) {
            Mail mail = new Mail(user.getId());
            mailRepository.save(mail);
        }
    }

    @Transactional
    @Override
    public void updateMailSendingCheck(Long id) {
        Mail mail = mailRepository.findById(id).orElse(null);
        if (mail != null) {
            mail.updateMailCheck();
            mailRepository.save(mail);
        } else {
            log.error("Could not update mail with id {}", id);
        }
    }

    @Transactional
    @Override
    public void updateMailDeleted(Long id) {
        Mail mail = mailRepository.findById(id).orElse(null);
        if (mail != null) {
            mail.updateMailDelete();
            mailRepository.save(mail);
        } else {
            log.error("Could not update mail with id {}", id);
        }
    }
}
