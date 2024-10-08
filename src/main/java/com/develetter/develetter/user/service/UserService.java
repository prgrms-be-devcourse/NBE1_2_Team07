package com.develetter.develetter.user.service;

import com.develetter.develetter.user.global.dto.request.*;
import com.develetter.develetter.user.global.dto.response.*;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignupResponseDto> signUp(SignupRequestDto dto);
    ResponseEntity<? super SigninResponseDto> signIn(SigninRequestDto dto);
    ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto);
    ResponseEntity<? super EditEmailResponseDto> editEmail(EditEmailRequestDto dto);
    Long getUserIdByEmail(String email);
}
