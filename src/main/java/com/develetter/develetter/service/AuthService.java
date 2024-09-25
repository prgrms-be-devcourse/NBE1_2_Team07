package com.develetter.develetter.service;

import com.develetter.develetter.dto.request.auth.EmailCertificationRequestDto;
import com.develetter.develetter.dto.request.auth.IdCheckRequestDto;
import com.develetter.develetter.dto.response.auth.EmailCertificationResponseDto;
import com.develetter.develetter.dto.response.auth.IdCheckResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService  {
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
}
