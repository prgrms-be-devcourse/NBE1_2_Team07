package com.develetter.develetter.mail.controller;

import com.develetter.develetter.mail.dto.MailDto;
import com.develetter.develetter.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/send-mail")
@RestController
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity sendMail(@RequestBody MailDto mailDto) {
        mailService.sendMail(mailDto, "email");
        return ResponseEntity.ok().build();
    }
}
