package com.develetter.develetter.provider;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProvider {

    private final JavaMailSender javaMailSender;
    private final String SUBJECT="[Develetter] Account Verification";

    public boolean sendVerificationEmail(String email,String certificationNumber) {
        try {
            MimeMessage message=javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper=new MimeMessageHelper(message, true);

            String htmlContent=getCertificationMessage(certificationNumber);

            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(message);

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getCertificationMessage(String certificationNumber) {
        String certificationMessage = "";
        certificationMessage+="<h1 style='text-align: center;'>Develetter 인증메일</h1><br>";
        certificationMessage+="<h3 style='text-align: center;'>당신의 인증코드: <strong>"+certificationNumber+"</strong></h3><br>";
        return certificationMessage;
    }
}
