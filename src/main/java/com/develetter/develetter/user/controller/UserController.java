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
    private final UserService userService;

    /**
     * ID 중복체크(id post 요청)
     * @param requestBody
     * @return
     */
    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck(@RequestBody @Valid IdCheckRequestDto requestBody) {
        ResponseEntity<? super IdCheckResponseDto> response= userService.idCheck(requestBody);
        return response;
    }

    /**
     * 이메일 인증코드 발송(id, email을 post요청)
     * @param requestBody
     * @return
     */
    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(@RequestBody @Valid EmailCertificationRequestDto requestBody) {
        ResponseEntity<? super EmailCertificationResponseDto> response= userService.emailCertification(requestBody);
        return response;
    }

    /**
     * 이메일 인증번호 검사(id, email, certificationCode post 요청)
     * @param requestBody
     * @return
     */
    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(@RequestBody @Valid CheckCertificationRequestDto requestBody) {
        ResponseEntity<? super CheckCertificationResponseDto> response= userService.checkCertification(requestBody);
        return response;
    }

    /**
     * 회원가입하기 (id, password, email, certificationNumber post 요청)
     * @param requestBody
     * @return
     */
    @PostMapping("/sign-up")
    public ResponseEntity<? super SignupResponseDto> signUp(@RequestBody @Valid SignupRequestDto requestBody) {
        ResponseEntity<? super SignupResponseDto> response= userService.signUp(requestBody);
        return response;
    }

    /**
     * 로그인하기 (id, password post 요청)
     * @param requestBody
     * @return
     */
    @PostMapping("/sign-in")
    public ResponseEntity<? super SigninResponseDto> signIn(@RequestBody @Valid SigninRequestDto requestBody) {
        ResponseEntity<? super SigninResponseDto> response= userService.signIn(requestBody);
        return response;
    }

    /**
     * 계정삭제하기 (id, password post 요청 -> deletemapping으로 변경 가능)
     * @param requestBody
     * @return
     */
    @PostMapping("/delete-user")
    public ResponseEntity<? super DeleteIdResponseDto> deleteUser(@RequestBody @Valid DeleteIdRequestDto requestBody) {
        ResponseEntity<? super DeleteIdResponseDto> response= userService.deleteId(requestBody);
        return response;
    }

}
