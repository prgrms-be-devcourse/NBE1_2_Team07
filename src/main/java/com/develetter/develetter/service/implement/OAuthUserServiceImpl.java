package com.develetter.develetter.service.implement;

import com.develetter.develetter.dto.entity.CustomOAuthUser;
import com.develetter.develetter.repository.UserRepository;
import com.develetter.develetter.dto.entity.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserEntity userEntity = null;
        String userId = null;
        String email="email@email.com";

        if(oauthClientName.equals("kakao")) {
            userId="kakao_" + oAuth2User.getAttributes().get("id");
            userEntity=new UserEntity(userId, email,"kakao");
        }

        userRepository.save(userEntity);

        return new CustomOAuthUser(userId);
    }
}
