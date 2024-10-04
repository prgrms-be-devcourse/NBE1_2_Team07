package com.develetter.develetter.mail.service;

import com.develetter.develetter.conference.entity.Conference;
import com.develetter.develetter.mail.converter.Converter;
import com.develetter.develetter.mail.dto.MailRegisterDto;
import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.mail.entity.Mail;
import com.develetter.develetter.mail.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MailServiceImpl implements MailService {

    private final MailRepository mailRepository;

    @Override
    public List<MailResDto> getAllMails() {
        return mailRepository.findAll()
                .stream().map(Converter::toDto)
                .toList();
    }

    @Override
    public void createMails() {
        // Step 1: JOIN 쿼리로 모든 userId와 연관된 데이터를 가져옴
//        List<Object[]> resultList = mailRepository.findAllUserWithFilteredData();
//
//        // Step 2: 결과 데이터를 mail 테이블에 저장
//        for (Object[] result : resultList) {
//            Long userId = (Long) result[0];
//            Long filteredJobPostingId = (Long) result[1];
//            Long filteredBlogId = (Long) result[2];
//
        //추후에 필요하지 않다고 판단되면 entity로 진행예정
//            MailRegisterDto mailRegisterDto = new MailRegisterDto(userId, filteredJobPostingId, filteredBlogId);
//            Mail mail = Converter.toEntity(mailRegisterDto);
//            mailRepository.save(mail);
//        }
    }

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

}
