package com.develetter.develetter.service.implement;

import com.develetter.develetter.dto.request.auth.EmailCertificationRequestDto;
import com.develetter.develetter.dto.request.auth.IdCheckRequestDto;
import com.develetter.develetter.dto.response.ResponseDto;
import com.develetter.develetter.dto.response.auth.EmailCertificationResponseDto;
import com.develetter.develetter.dto.response.auth.IdCheckResponseDto;
import com.develetter.develetter.provider.EmailProvider;
import com.develetter.develetter.repository.CertificationRepository;
import com.develetter.develetter.repository.UserRepository;
import com.develetter.develetter.service.AuthService;
import com.develetter.develetter.user.CertificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CertificationRepository certificationRepository;
    private final EmailProvider emailProvider;

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

    private String getCertificationNumber() {
        String certificationNumber = "";

        for(int count=0;count<4;count++)
            certificationNumber+=(int)(Math.random()*10);

        return certificationNumber;
    }
}
