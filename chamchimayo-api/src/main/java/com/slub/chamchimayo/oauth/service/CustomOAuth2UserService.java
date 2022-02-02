package com.slub.chamchimayo.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slub.chamchimayo.dto.SessionUser;
import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfo;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfoFactory;
import com.slub.chamchimayo.repository.UserRepository;

import java.util.Collections;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

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
        User user = saveOrUpdate(userInfo, providerType);
        httpSession.setAttribute("user", new SessionUser(user)); //SessionUser(직렬화된 dto 클래스 사용)

        log.info("User Login Info : {}", user);

        return new DefaultOAuth2User(
            Collections.singleton(
                new SimpleGrantedAuthority(user.getRoleKey())),
                userInfo.getAttributes(),
                userNameAttributeName);
    }

    // 유저 생성 및 수정 서비스 로직
    private User saveOrUpdate(OAuth2UserInfo userInfo, ProviderType providerType){
        User user = userRepository.findByEmailAndProviderType(userInfo.getEmail(), providerType)
            .map(entity -> entity.updateUser(userInfo.getName(), userInfo.getGender()))
            .orElse(userInfo.toEntity());
        return userRepository.save(user);
    }
}