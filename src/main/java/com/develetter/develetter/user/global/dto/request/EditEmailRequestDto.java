package com.develetter.develetter.user.global.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EditEmailRequestDto {
    @NotBlank
    private String oauthId;

    @Email
    @NotBlank
    private String newEmail;
}
