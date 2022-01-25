package com.slub.chamchimayo.oauth.info;

import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.info.impl.KakaoOAuth2UserInfo;
import com.slub.chamchimayo.oauth.info.impl.NaverOAuth2UserInfo;
import com.slub.chamchimayo.oauth.info.impl.GoogleOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("잘못된 Provider Type");
        }
    }
}
