package com.develetter.develetter.user.provider;

public class CertificationNumberProvider {
    public static String generateNumber() {
        String number = "";

        for (int count = 0; count < 4; count++)
            number += (int) (Math.random() * 10);

        return number;
    }
}
