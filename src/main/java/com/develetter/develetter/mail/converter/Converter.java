package com.develetter.develetter.mail.converter;

import com.develetter.develetter.mail.dto.MailResDto;
import com.develetter.develetter.mail.entity.Mail;

public class Converter {

    // Entity -> DTO
    public static MailResDto toDto(Mail mail) {
        return new MailResDto(
                mail.getId(),
                mail.getUserId(),
                mail.getFilteredJobPostingId(),
                mail.getFilteredBlogId(),
                mail.getSendingCheck(),
                mail.getDeleted()
        );
    }
}
