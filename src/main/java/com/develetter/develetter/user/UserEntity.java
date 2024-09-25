package com.develetter.develetter.user;

import com.develetter.develetter.dto.request.auth.SignupRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="user")
@Table(name = "user")
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private String email;
    private String type;
    private String role;

    public UserEntity(SignupRequestDto dto) {
        this.userId = dto.getId();
        this.password = dto.getPassword();
        this.email = dto.getEmail();
        this.type = "app";
        this.role = "ROLE_USER";
    }
}
