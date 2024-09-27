package com.develetter.develetter.user.service.implement;

import com.develetter.develetter.user.dto.request.*;
import com.develetter.develetter.user.dto.response.*;
import com.develetter.develetter.user.global.common.CertificationNumber;
import com.develetter.develetter.user.provider.EmailProvider;
import com.develetter.develetter.user.provider.JwtProvider;
import com.develetter.develetter.user.repository.CertificationRepository;
import com.develetter.develetter.user.repository.UserRepository;
import com.develetter.develetter.user.service.UserService;
import com.develetter.develetter.user.dto.entity.CertificationEntity;
import com.develetter.develetter.user.dto.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증, 회원 가입, 이메일 인증 등을 처리하며 예외 상황에 대한 대응 로직도 포함.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    // 비밀번호 인코더 (BCrypt 방식 사용)
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * ID 중복 여부를 체크하는 메서드.
     * @param dto ID 중복 체크 요청 DTO
     * @return 중복 여부에 따른 응답
     */
    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) return IdCheckResponseDto.duplicateId();  // 중복된 ID인 경우 응답
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();  // 중복되지 않은 경우 성공 응답
    }

    /**
     * 이메일 인증을 위한 인증 번호를 전송하는 메서드.
     * @param dto 이메일 인증 요청 DTO
     * @return 이메일 전송 성공 여부에 따른 응답
     */
    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();

            // 중복된 ID인지 확인
            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) return EmailCertificationResponseDto.duplicateId();

            // 인증 번호 생성 및 이메일 전송
            String certificationNumber = CertificationNumber.getCertificationNumber();
            boolean isSendSuccess = emailProvider.sendVerificationEmail(email, certificationNumber);
            if (!isSendSuccess) return EmailCertificationResponseDto.mailSendFail();

            // 인증 엔티티 저장
            CertificationEntity certificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();  // 이메일 전송 성공 응답
    }

    /**
     * 인증 번호 확인 메서드.
     * @param dto 인증 번호 확인 요청 DTO
     * @return 인증 성공 여부에 따른 응답
     */
    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            // 인증 엔티티 조회
            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if (certificationEntity == null) return CheckCertificationResponseDto.certificationFail();

            // 이메일 및 인증 번호가 일치하는지 확인
            boolean isMatch = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatch) return CheckCertificationResponseDto.certificationFail();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();  // 인증 성공 응답
    }

    /**
     * 회원 가입 처리 메서드.
     * @param dto 회원 가입 요청 DTO
     * @return 회원 가입 성공 여부에 따른 응답
     */
    @Override
    public ResponseEntity<? super SignupResponseDto> signUp(SignupRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if (isExistId) return SignupResponseDto.duplicateId();  // 중복된 ID 확인

            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            // 인증 번호 확인
            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            boolean isMatched = certificationEntity.getEmail().equals(email) &&
                    certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return SignupResponseDto.certificationFail();

            // 비밀번호 암호화 후 저장
            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            // 사용자 엔티티 저장
            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            // 인증 엔티티 삭제(계속 남겨둘수도있음)
            certificationRepository.deleteByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignupResponseDto.success();  // 회원 가입 성공 응답
    }

    /**
     * 로그인 처리 메서드.
     * @param dto 로그인 요청 DTO
     * @return 로그인 성공 여부 및 JWT 토큰
     */
    @Override
    public ResponseEntity<? super SigninResponseDto> signIn(SigninRequestDto dto) {
        String token = null;

        try {
            String userId = dto.getId();
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) return SigninResponseDto.signInFail();  // 사용자 조회 실패

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return SigninResponseDto.signInFail();  // 비밀번호 불일치

            // JWT 토큰 생성
            token = jwtProvider.create(userId);

        } catch (Exception e) {
            e.printStackTrace();
            return SigninResponseDto.signInFail();
        }
        return SigninResponseDto.success(token);  // 로그인 성공 및 토큰 반환
    }

    /**
     * 사용자 계정 삭제 처리 메서드.
     * @param dto 계정 삭제 요청 DTO
     * @return 삭제 성공 여부에 따른 응답
     */
    @Override
    public ResponseEntity<? super DeleteIdResponseDto> deleteId(DeleteIdRequestDto dto) {
        try {
            String userId = dto.getId();
            UserEntity userEntity = userRepository.findByUserId(userId);
            if (userEntity == null) return DeleteIdResponseDto.idNotFound();  // 사용자 조회 실패

            // 비밀번호 일치 확인
            String password = dto.getPassword();
            String encodedPassword = userEntity.getPassword();
            boolean isMatched = passwordEncoder.matches(password, encodedPassword);
            if (!isMatched) return DeleteIdResponseDto.idNotMatching();  // 비밀번호 불일치

            // 사용자 엔티티 및 인증 엔티티 삭제
            userRepository.delete(userEntity);
            certificationRepository.deleteByUserId(userId);

        } catch (Exception e) {
            e.printStackTrace();
            return DeleteIdResponseDto.databaseError();  // 데이터베이스 오류 처리
        }
        return DeleteIdResponseDto.success();  // 삭제 성공 응답
    }
}
