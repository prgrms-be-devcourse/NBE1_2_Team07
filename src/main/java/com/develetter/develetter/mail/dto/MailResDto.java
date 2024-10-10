package com.develetter.develetter.mail.dto;

import java.time.LocalDate;

public record MailResDto(
        Long id,
        Long userId,
        Boolean sendingCheck,
        Boolean deleted
) {
}
