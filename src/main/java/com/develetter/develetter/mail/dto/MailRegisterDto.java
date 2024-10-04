package com.develetter.develetter.mail.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;

public record MailRegisterDto (
        Long userId,
        Long filteredJobPostingId,
        Long filteredBlogId
) {}