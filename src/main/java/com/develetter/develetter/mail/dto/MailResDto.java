package com.develetter.develetter.mail.dto;

import java.time.LocalDate;

public record MailResDto(
        Long id,
        Long userId,
        Long filteredJobPostingId,
        Long filteredBlogId,
        Boolean sendingCheck
) {
}
