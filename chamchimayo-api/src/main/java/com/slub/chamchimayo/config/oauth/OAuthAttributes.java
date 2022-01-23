package com.slub.chamchimayo.config.oauth;

import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.entity.enums.ProviderType;
import com.slub.chamchimayo.entity.enums.Role;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 MAP
    private String nameAttributeKey;
    private String name;
    private String email;
    private String gender;
    private String mobile;
    private ProviderType providerType;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
        String name, String email, String gender, String mobile, ProviderType providerType) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.providerType = providerType;
    }

    public static OAuthAttributes of(ProviderType providerType, String userNameAttributeName,
        Map<String, Object> attributes) {

        if (ProviderType.KAKAO.equals(providerType)) {
            return ofKakao("id", attributes, ProviderType.KAKAO);
        }
        if (ProviderType.NAVER.equals(providerType)) {
            return ofNaver("id", attributes, ProviderType.NAVER);
        }
        return ofGoogle(userNameAttributeName, attributes, ProviderType.GOOGLE);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes, ProviderType providerType) {
        return OAuthAttributes.builder()
            .name((String)attributes.get("name"))
            .email((String)attributes.get("email"))
            .providerType(providerType)
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes, ProviderType providerType) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

        return OAuthAttributes.builder()
            .name((String)response.get("name"))
            .email((String)response.get("email"))
            .gender((String)response.get("gender"))
            .mobile((String)response.get("mobile"))
            .providerType(providerType)
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes, ProviderType providerType) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
            .name((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .gender((String) kakaoAccount.get("gender"))
            .providerType(providerType)
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    public User toEntity() {
        return User.builder()
            .name(name)
            .email(email)
            .gender(gender)
            .mobile(mobile)
            .providerType(providerType)
            .role(Role.USER) //기본 권한 USER
            .build();
    }
}
