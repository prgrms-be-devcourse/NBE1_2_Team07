package com.develetter.develetter.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class EmailCertificationRequestDto {
    @NotBlank
    private String id;

    @Email
    @NotBlank
    private String email;
}
