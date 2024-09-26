package com.develetter.develetter.service;

import com.develetter.develetter.dto.request.auth.*;
import com.develetter.develetter.dto.response.auth.*;
import org.springframework.http.ResponseEntity;

public interface AuthService  {
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignupResponseDto> signUp(SignupRequestDto dto);
    ResponseEntity<? super SigninResponseDto> signIn(SigninRequestDto dto);
}
