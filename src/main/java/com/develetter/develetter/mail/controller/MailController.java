package com.develetter.develetter.mail.controller;

import com.develetter.develetter.mail.dto.MailDto;
import com.develetter.develetter.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/send-mail")
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    public ResponseEntity sendMail(@RequestBody MailDto mailDto) {
        mailService.sendMail(mailDto, "email");
        return ResponseEntity.ok().build();
    }
}
