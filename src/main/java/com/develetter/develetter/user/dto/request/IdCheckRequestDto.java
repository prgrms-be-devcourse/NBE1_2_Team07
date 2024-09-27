package com.develetter.develetter.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class IdCheckRequestDto {
    @NotBlank
    private String id;
}
