package com.slub.chamchimayo.config.oauth;

import com.slub.chamchimayo.dto.SessionUser;
import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.entity.enums.ProviderType;
import com.slub.chamchimayo.repository.UserRepository;
import java.util.Collections;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // OAuth2 서비스 id (네이버, 카카오)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase(Locale.ROOT));
        log.info("registrationId {}, providerType {}", registrationId, providerType);

        // OAuth2 로그인 진행 시 키가 되는 필드 값 (pk)
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                        .getUserInfoEndpoint().getUserNameAttributeName();

        log.info("userNameAttributeName {}", userNameAttributeName);

        // OAuth2UserService
        OAuthAttributes attributes = OAuthAttributes.of(providerType, userNameAttributeName, oAuth2User.getAttributes());
        User user = saveOrUpdate(attributes, providerType);
        httpSession.setAttribute("user", new SessionUser(user)); //SessionUser(직렬화된 dto 클래스 사용)

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                                        attributes.getAttributes(),
                                        attributes.getNameAttributeKey());
    }

    // 유저 생성 및 수정 서비스 로직
    private User saveOrUpdate(OAuthAttributes attributes, ProviderType providerType){
        User user = userRepository.findByEmailAndProviderType(attributes.getEmail(), providerType)
            .map(entity -> entity.update(attributes.getName(), attributes.getGender(), attributes.getMobile()))
            .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
