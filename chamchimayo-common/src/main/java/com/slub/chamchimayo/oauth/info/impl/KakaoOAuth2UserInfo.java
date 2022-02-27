package com.slub.chamchimayo.oauth.info.impl;

import com.slub.chamchimayo.oauth.info.OAuth2UserInfo;

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
    public String getId() {
        return attributes.get("id").toString();
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
        String gender = (String) kakaoAccount.get("gender");

        if ("male".equals(gender)) {
            gender = "M";
        } else {
            gender = "F";
        }
        return gender;
    }

    @Override
    public String getMobile() {
        return (String)kakaoAccount.get("mobile");
    }
}
