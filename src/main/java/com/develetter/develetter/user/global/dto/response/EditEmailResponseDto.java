package com.develetter.develetter.user.global.dto.response;

import com.develetter.develetter.user.global.common.ResponseCode;
import com.develetter.develetter.user.global.common.ResponseMessage;
import com.develetter.develetter.user.global.dto.LogInResponseDto;
import org.springframework.http.ResponseEntity;

public class EditEmailResponseDto extends LogInResponseDto {
    private EditEmailResponseDto(){
        super();
    }

    public static ResponseEntity<LogInResponseDto> emailNotFound(){
        LogInResponseDto responseBody = new LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(404).body(responseBody);
    }

}
