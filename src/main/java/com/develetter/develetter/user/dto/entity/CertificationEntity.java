package com.develetter.develetter.user.dto.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="certification" )
@Table(name = "certification"  )
/**
 * 이메일 검증을 위한 Entity
 */
public class CertificationEntity {
    @Id
    private String userId;
    private String email;
    private String certificationNumber;
}
