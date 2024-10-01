package com.develetter.develetter.user.global.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id", nullable = false, length = 30)
    private String accountId;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "certification_number", nullable = false, length = 4)
    private String certificationNumber;
}
