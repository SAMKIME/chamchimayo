package com.slub.chamchimayo.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.oauth.entity.UserPrincipal;
import com.slub.chamchimayo.oauth.exception.OAuthProviderMissMatchException;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfo;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfoFactory;
import com.slub.chamchimayo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return this.process(userRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oAuth2User) throws JsonProcessingException {
        // OAuth2 서비스 id (구글, 네이버, 카카오)
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        log.info("ProviderType : {}", providerType); //GOOGLE, NAVER, KAKAO

        // OAuth2 로그인 진행 시 키가 되는 필드 값 (pk)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("userNameAttributeName : {}", userNameAttributeName); //sub, response, id

        // OAuth2UserService
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2User.getAttributes());
        log.info("UserInfo attributes Check before save : {}", userInfo.getAttributes());

        User savedUser = userRepository.findByUserId(userInfo.getId()).orElse(null);

        log.info("Find User Info Check before save: {}", savedUser);

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                    "Looks like you're signed up with " + providerType +
                        " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
        }

        log.info("Save Or Update After User Login Info : {}", savedUser);

        return UserPrincipal.create(savedUser, oAuth2User.getAttributes());
    }

    /**
     * 유저 생성 및 수정 서비스 로직S
     */
    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        User user = User.builder()
            .userId(userInfo.getId())
            .name(userInfo.getName())
            .email(userInfo.getEmail())
            .providerType(providerType)
            .gender(userInfo.getGender())
            .mobile(userInfo.getMobile())
            .roleType(RoleType.USER)
            .build();

        return userRepository.save(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getName().equals(userInfo.getName())) {
            user.changeName(userInfo.getName());
        }
        return user;
    }
}
