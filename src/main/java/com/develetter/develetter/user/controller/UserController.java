package com.develetter.develetter.user.controller;

import com.develetter.develetter.user.dto.request.*;
import com.develetter.develetter.user.dto.response.*;
import com.develetter.develetter.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService authService;

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

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(@RequestBody @Valid CheckCertificationRequestDto requestBody) {
        ResponseEntity<? super CheckCertificationResponseDto> response=authService.checkCertification(requestBody);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignupResponseDto> signUp(@RequestBody @Valid SignupRequestDto requestBody) {
        ResponseEntity<? super SignupResponseDto> response=authService.signUp(requestBody);
        return response;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<? super SigninResponseDto> signIn(@RequestBody @Valid SigninRequestDto requestBody) {
        ResponseEntity<? super SigninResponseDto> response=authService.signIn(requestBody);
        return response;
    }

    @PostMapping("/delete-user")
    public ResponseEntity<? super DeleteIdResponseDto> deleteUser(@RequestBody @Valid DeleteIdRequestDto requestBody) {
        ResponseEntity<? super DeleteIdResponseDto> response=authService.deleteId(requestBody);
        return response;
    }

}
