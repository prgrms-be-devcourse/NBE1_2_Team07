package com.develetter.develetter.user.controller;

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
        log.info("[idCheck]: {id:" + requestBody.getId()+ "}");
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
        log.info("[emailCertification]: {id: " + requestBody.getId() + ", email: " + requestBody.getEmail() + "}");
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
        log.info("[checkCertification]: {id: " + requestBody.getId() + ", email: " + requestBody.getEmail() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
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
        log.info("[signUp]: {id: " + requestBody.getId() + ", password: " + requestBody.getPassword() + ", email: " + requestBody.getEmail() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
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
        log.info("[signIn]: {id: " + requestBody.getId() + ", password: " + requestBody.getPassword() + "}");
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
        log.info("[deleteUser]: {id: " + requestBody.getId() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

    /**
     * 카카오 또는 네이버 로그인 처리
//     * @param requestDto OAuth 로그인 요청 데이터 (provider와 accessToken 포함)
//     * @return JWT 토큰과 메시지를 담은 응답
//     */
//    @PostMapping("/oauth2/login")
//    public ResponseEntity<OAuthLoginResponseDto> oauthLogin(@RequestBody @Valid OAuthLoginRequestDto requestDto) {
//        // 소셜 로그인 처리
//        String token = userService.oauthLogin(requestDto);
//
//        // JWT 토큰과 로그인 성공 메시지 반환
//        OAuthLoginResponseDto responseDto = new OAuthLoginResponseDto(token, "Login successful");
//        log.info("[oauthLogin]: User logged in successfully with provider {}", requestDto.getProvider());
//        return ResponseEntity.ok(responseDto);
//    }
//
//    /**
//     * OAuth2 로그인 성공 후 호출되는 엔드포인트
//     * @param authentication 인증 객체
//     * @return JWT 토큰과 메시지를 담은 응답 객체
//     */
//    @GetMapping("/oauth2/success")
//    public ResponseEntity<? super OAuthLoginResponseDto> oauth2Success(Authentication authentication) {
//        // 인증된 사용자 정보 가져오기
//        String userId = authentication.getName();
//        String token = userService.generateToken(userId);
//
//        // 응답 DTO 생성
//        OAuthLoginResponseDto responseDto = new OAuthLoginResponseDto(token, "Login successful");
//
//        log.info("[oauth2Success]: User {} logged in successfully", userId);
//        return ResponseEntity.ok(responseDto);
//    }
//}

}
