package com.develetter.develetter.service.implement;

import com.develetter.develetter.dto.request.auth.*;
import com.develetter.develetter.dto.response.ResponseDto;
import com.develetter.develetter.dto.response.auth.*;
import com.develetter.develetter.provider.EmailProvider;
import com.develetter.develetter.provider.JwtProvider;
import com.develetter.develetter.repository.CertificationRepository;
import com.develetter.develetter.repository.UserRepository;
import com.develetter.develetter.service.AuthService;
import com.develetter.develetter.user.CertificationEntity;
import com.develetter.develetter.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;

    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String userId=dto.getId();
            boolean isExistId=userRepository.existsByUserId(userId);
            if(isExistId) return IdCheckResponseDto.duplicateId();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return IdCheckResponseDto.success();
    }

    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String userId=dto.getId();
            String email=dto.getEmail();

            boolean isExistId=userRepository.existsByUserId(userId);
            if(!isExistId) return EmailCertificationResponseDto.duplicateId();

            String certificationNumber=getCertificationNumber();

            boolean isSendSuccess=emailProvider.sendVerificationEmail(email, certificationNumber);
            if(!isSendSuccess) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity certificationEntity=new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(certificationEntity);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return EmailCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String userId=dto.getId();
            String email=dto.getEmail();
            String certificationNumber=dto.getCertificationNumber();

            CertificationEntity certificationEntity=certificationRepository.findByUserId(userId);
            if(certificationEntity==null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatch=certificationEntity.getEmail().equals(email) && getCertificationNumber().equals(certificationNumber);
            if(!isMatch) return CheckCertificationResponseDto.certificationFail();



        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignupResponseDto> signUp(SignupRequestDto dto) {
        try {
            String userId=dto.getId();
            boolean isExistId=userRepository.existsByUserId(userId);
            if(isExistId) return SignupResponseDto.duplicateId();

            String email=dto.getEmail();
            String certificationNumber=dto.getCertificationNumber();

            CertificationEntity certificationEntity=certificationRepository.findByUserId(userId);
            boolean isMatched= certificationEntity.getEmail().equals(email) && getCertificationNumber().equals(certificationNumber);
            if(!isMatched) return SignupResponseDto.certificationFail();

            String password=dto.getPassword();
            String encodedPassword=passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity=new UserEntity(dto);
            userRepository.save(userEntity);

//            certificationRepository.delete(certificationEntity);
            certificationRepository.deleteByUserId(userId);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignupResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SigninResponseDto> signIn(SigninRequestDto dto) {
        String token=null;

        try {
            String userId=dto.getId();
            UserEntity userEntity=userRepository.findByUserId(userId);
            if(userEntity==null) return SigninResponseDto.signInFail();

            String password=dto.getPassword();
            String encodedPassword=userEntity.getPassword();
            boolean isMatched=passwordEncoder.matches(password, encodedPassword);
            if(!isMatched) return SigninResponseDto.signInFail();

            token=jwtProvider.create(userId);

        }catch (Exception e) {
            e.printStackTrace();
            return SigninResponseDto.signInFail();
        }
        return SigninResponseDto.success(token);
    }


    private String getCertificationNumber() {
        String certificationNumber = "";

        for(int count=0;count<4;count++)
            certificationNumber+=(int)(Math.random()*10);

        return certificationNumber;
    }
}
