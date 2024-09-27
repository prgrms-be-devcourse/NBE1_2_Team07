package com.develetter.develetter.user.global.common;


// 이메일 인증을 위해 4자리 랜덤숫자 생성기(클래스 없애고싶으시면 말씀해주십쇼)
public class CertificationNumber {
    public static String getCertificationNumber() {
        String certificationNumber = "";

        for (int count = 0; count < 4; count++)
            certificationNumber += (int) (Math.random() * 10);

        return certificationNumber;
    }
}
