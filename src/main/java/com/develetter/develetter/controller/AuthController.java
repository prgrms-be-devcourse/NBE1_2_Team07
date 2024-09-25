package com.develetter.develetter.controller;

import com.develetter.develetter.dto.request.auth.EmailCertificationRequestDto;
import com.develetter.develetter.dto.request.auth.IdCheckRequestDto;
import com.develetter.develetter.dto.response.auth.EmailCertificationResponseDto;
import com.develetter.develetter.dto.response.auth.IdCheckResponseDto;
import com.develetter.develetter.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(@RequestBody @Valid IdCheckRequestDto requestBody) {
        ResponseEntity<? super IdCheckResponseDto> response=authService.idCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(@RequestBody @Valid EmailCertificationRequestDto requestBody) {
        ResponseEntity<? super EmailCertificationResponseDto> response=authService.emailCertification(requestBody);
        return response;
    }
}
