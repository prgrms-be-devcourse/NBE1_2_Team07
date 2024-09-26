package com.develetter.develetter.mail.dto;

//public record UserDTO(
//    Long id,
//    String email,
//    int filterId
//) {
//    public static UserDTO of(Long id, String email, int filterId) {
//        return new UserDTO(id, email, filterId);
//    }
//}

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}