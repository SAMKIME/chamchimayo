package com.slub.chamchimayo.entity.oauth.info.impl;

import com.slub.chamchimayo.entity.oauth.info.OAuth2UserInfo;
import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    private static Map<String, Object> response ;

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {

        super(attributes);
        response = (Map<String, Object>)attributes.get("response");
    }

    @Override
    public String getName() {
        return (String)response.get("name");
    }

    @Override
    public String getEmail() {
        return (String)response.get("email");
    }

    @Override
    public String getGender() {
        return (String)response.get("gender");
    }

    public String getMobile() {
        return (String)response.get("mobile");
    }
}
