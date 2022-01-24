package com.slub.chamchimayo.entity.oauth.info.impl;

import com.slub.chamchimayo.entity.oauth.info.OAuth2UserInfo;
import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    private static Map<String, Object> kakaoAccount;
    private static Map<String, Object> kakaoProfile;


    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");
    }

    @Override
    public String getName() {
        return (String) kakaoProfile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getGender() {
        return (String) kakaoAccount.get("gender");
    }
}
