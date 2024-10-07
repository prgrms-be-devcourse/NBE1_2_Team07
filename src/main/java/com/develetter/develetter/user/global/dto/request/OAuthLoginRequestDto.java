package com.develetter.develetter.user.global.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class OAuthLoginRequestDto {

    @NotBlank
    private String provider; // 'kakao' 또는 'naver'

    @NotBlank
    private String accessToken; // 카카오 또는 네이버에서 제공하는 OAuth2 access token
}