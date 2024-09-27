package com.develetter.develetter.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SigninRequestDto {
    @NotBlank
    private String id;

    @NotBlank
    private String password;
}
