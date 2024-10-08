package com.develetter.develetter.user.controller;

import com.develetter.develetter.user.global.dto.LogInResponseDto;
import com.develetter.develetter.user.global.dto.request.*;
import com.develetter.develetter.user.global.dto.response.*;
import com.develetter.develetter.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
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
        log.info("[idCheck]: {id:" + requestBody.getEmail()+ "}");
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
        log.info("[emailCertification]: {id: " + requestBody.getEmail() + ", email: " + requestBody.getEmail() + "}");
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
        log.info("[checkCertification]: {id: " + requestBody.getEmail() + ", email: " + requestBody.getEmail() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
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
        log.info("[signUp]: {id: " + requestBody.getEmail() + ", password: " + requestBody.getPassword() + ", email: " + requestBody.getEmail() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
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
        log.info("[signIn]: {id: " + requestBody.getEmail() + ", password: " + requestBody.getPassword() + "}");
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
        log.info("[deleteUser]: {id: " + requestBody.getEmail() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

    @PostMapping("/get-id")
    public ResponseEntity<Long> getIdByEmail(@RequestParam String email) {
        Long userId = userService.getUserIdByEmail(email);
        log.info("[getIdByEmail]: email: " + email);
        return ResponseEntity.ok(userId);
    }

    @PostMapping("/edit-email")
    public ResponseEntity<? super EditEmailResponseDto> editEmail(@RequestBody @Valid EditEmailRequestDto requestBody) {
        ResponseEntity<? super EditEmailResponseDto> response = userService.editEmail(requestBody);
        log.info("[editEmail]: {id: " + requestBody.getOauthId() + ", newEmail: " + requestBody.getNewEmail() + "}");
        return response;
    }

}
