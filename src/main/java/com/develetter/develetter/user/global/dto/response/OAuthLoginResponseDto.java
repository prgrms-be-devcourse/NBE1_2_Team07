package com.develetter.develetter.user.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class OAuthLoginResponseDto {
    private String token;
    private String message;

    private OAuthLoginResponseDto(String token, String message) {
        super();
        this.token = token;
        this.message = message;
    }

    public static ResponseEntity<OAuthLoginResponseDto> success(String token) {
        OAuthLoginResponseDto responseBody = new OAuthLoginResponseDto(token, "Login successful");
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<OAuthLoginResponseDto> signInFail(String message) {
        OAuthLoginResponseDto responseBody = new OAuthLoginResponseDto(null, message);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}
