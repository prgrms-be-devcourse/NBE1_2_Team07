package com.develetter.develetter.user.global.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2User 커스텀을 위한 핸들
 */
@NoArgsConstructor
@AllArgsConstructor
public class CustomOAuthUser implements OAuth2User {

    private Long id; // Long 타입의 사용자 ID 추가
    private String accountId;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.accountId;
    }

    // Long 타입의 id를 반환하는 getId 메서드 추가
    public Long getId() {
        return this.id;
    }
}