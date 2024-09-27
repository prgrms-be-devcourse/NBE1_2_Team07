package com.develetter.develetter.user.dto.response;

import com.develetter.develetter.user.global.common.ResponseCode;
import com.develetter.develetter.user.global.common.ResponseMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public class DeleteIdResponseDto extends ResponseDto {

    private DeleteIdResponseDto(){
        super();
    }

    public static ResponseEntity<DeleteIdResponseDto> success(){
        DeleteIdResponseDto responseBody = new DeleteIdResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> idNotFound(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> idNotMatching(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.ID_NOT_MATCHING, ResponseMessage.ID_NOT_MATCHING);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }

}
