package com.develetter.develetter.user.service.implement;

import com.develetter.develetter.user.global.entity.CustomOAuthUser;
import com.develetter.develetter.user.global.entity.UserEntity;
import com.develetter.develetter.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * OAuth2 사용자 정보를 처리하는 서비스 구현체.
 * 카카오, 네이버와 같은 OAuth 제공자의 사용자 정보를 받아 사용자 엔티티를 생성하고 저장.
 */
@Service
@RequiredArgsConstructor
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * OAuth2 인증 후 사용자 정보를 처리하는 메서드.
     * 제공자에 따라 사용자 ID를 생성하고, 해당 정보를 기반으로 사용자 엔티티를 저장.
     * @param request OAuth2UserRequest 객체 (클라이언트 등록 정보 포함)
     * @return CustomOAuthUser 객체 (사용자 정보 포함)
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        // 기본 OAuth2UserService를 사용해 사용자 정보 로드
        OAuth2User oAuth2User = super.loadUser(request);

        // 어떤 OAuth 클라이언트를 사용했는지 확인 (예: 카카오, 네이버)
        String oauthClientName = request.getClientRegistration().getClientName();
        String oauthClientId = request.getClientRegistration().getClientId();

        // 사용자 엔티티 및 ID 변수 선언
        String accountId = null;
        String password = passwordEncoder.encode(oauthClientId);  // 각 ClientId를 비밀번호로 Encrypt
        String email = oauthClientId + "@example.com";  // 기본 이메일 설정
        String providerType = oauthClientName.equals("kakao") ? "kakao" : "naver";

        // OAuth 제공자에 따라 계정 ID 설정
        if (providerType.equals("kakao")) {
            accountId = "kakao_" + oAuth2User.getAttributes().get("id");
        } else if (providerType.equals("naver")) {
            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            accountId = "naver_" + responseMap.get("id").substring(0, 14);
            email = responseMap.get("email");
        }
        // 사용자가 이미 존재하는 경우, 기존 엔티티를 사용하고 그렇지 않으면 새로 생성하여 저장
        // 기존 사용자 검색 및 처리

        // 기존 사용자 검색 및 처리
        UserEntity userEntity = userRepository.findByAccountId(accountId);

        if (userEntity == null) {
            // 새로운 사용자 엔티티 생성 후 저장
            userEntity = UserEntity.builder()
                    .accountId(accountId)
                    .password(password)
                    .email(email)
                    .type(providerType)
                    .role("ROLE_USER")
                    .subscription("NO")
                    .build();
            userEntity = userRepository.save(userEntity);
        }
        // 사용자 정보를 포함한 CustomOAuthUser 객체 반환
        return new CustomOAuthUser(userEntity.getId(), accountId);
    }
}