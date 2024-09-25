package com.develetter.develetter.dto.response.auth;

import com.develetter.develetter.common.ResponseCode;
import com.develetter.develetter.common.ResponseMessage;
import com.develetter.develetter.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public class SigninResponseDto extends ResponseDto {
    private String token;
    private int expirationTime;

    private SigninResponseDto(String token) {
        super();
        this.token = token;
        this.expirationTime=3600; // 1 hour
    }

    public static ResponseEntity<SigninResponseDto> success(String token) {
        SigninResponseDto responseBody = new SigninResponseDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFail(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }




}
