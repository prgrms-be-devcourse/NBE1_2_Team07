package com.develetter.develetter.mail.dto;

public record MailDto (
    String to,
    String subject,
    String body
) {
    public static MailDto of(String to, String subject, String body) {
        return new MailDto(to, subject, body);
    }
}
