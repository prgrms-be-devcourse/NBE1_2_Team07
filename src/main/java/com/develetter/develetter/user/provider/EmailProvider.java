package com.develetter.develetter.user.provider;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProvider {

    private final JavaMailSender javaMailSender;  // 이메일 전송을 위한 JavaMailSender 객체
    private final String SUBJECT = "[Develetter] 인증 메일";  // 이메일 제목

    /**
     * 인증 이메일을 전송하는 메서드.
     * 입력된 이메일 주소로 인증 번호가 포함된 HTML 이메일을 발송.
     * @param email               수신자의 이메일 주소
     * @param certificationNumber 인증 번호
     * @return 이메일 전송 성공 여부 (true: 성공, false: 실패)
     */
    public boolean sendVerificationEmail(String email, String certificationNumber) {

        try {
            // MIME 타입의 이메일 메시지 생성
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);

            // 인증 번호가 포함된 HTML 내용 생성
            String htmlContent = getCertificationMessage(certificationNumber);

            // 이메일 수신자, 제목 및 본문 설정
            messageHelper.setTo(email);
            messageHelper.setSubject(SUBJECT);
            messageHelper.setText(htmlContent, true);  // HTML 형식의 본문 설정

            // 이메일 전송
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;  // 전송 성공 시 true 리턴
    }

    private String getCertificationMessage(String certificationNumber) {
        String certificationMessage = "";
        certificationMessage += "<div style='max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; color: #333; border: 1px solid #eaeaea; border-radius: 10px;'>";

        // 헤더
        certificationMessage += "<div style='background-color: #4CAF50; padding: 20px; border-top-left-radius: 10px; border-top-right-radius: 10px;'>";
        certificationMessage += "<h1 style='text-align: center; color: #fff; margin: 0; font-size: 24px;'>Develetter</h1>";
        certificationMessage += "</div>";

        // 본문
        certificationMessage += "<div style='padding: 20px;'>";
        certificationMessage += "<h2 style='text-align: center; color: #333; font-size: 22px;'>인증 메일</h2>";
        certificationMessage += "<p style='text-align: center; font-size: 16px; line-height: 1.6;'>Develetter 서비스를 이용해주셔서 감사합니다.<br>아래 인증 코드를 입력하여 회원가입을 완료해주세요.</p>";
        certificationMessage += "<div style='text-align: center; margin: 30px 0;'>";
        certificationMessage += "<span style='display: inline-block; padding: 15px 30px; font-size: 24px; color: #fff; background-color: #4CAF50; border-radius: 5px;'>" + certificationNumber + "</span>";
        certificationMessage += "</div>";
        certificationMessage += "<p style='text-align: center; font-size: 14px; color: #777;'>인증 과정에서 문제가 발생하면 데브코스 7팀으로 문의해 주세요.</p>";
        certificationMessage += "</div>";

        // 푸터
        certificationMessage += "<div style='background-color: #f9f9f9; padding: 15px; text-align: center; border-bottom-left-radius: 10px; border-bottom-right-radius: 10px;'>";
        certificationMessage += "<p style='font-size: 12px; color: #777; margin: 0;'>© 2024 Develetter. All rights reserved.</p>";
        certificationMessage += "<p style='font-size: 12px; color: #777; margin: 5px 0;'>문의: devcourse7team@develetter.com</p>";
        certificationMessage += "</div>";

        certificationMessage += "</div>";
        return certificationMessage;
    }


}
