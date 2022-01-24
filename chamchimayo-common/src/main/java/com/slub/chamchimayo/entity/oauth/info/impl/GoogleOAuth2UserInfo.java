package com.slub.chamchimayo.entity.oauth.info.impl;

import com.slub.chamchimayo.entity.oauth.info.OAuth2UserInfo;
import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getGender() {
        return (String) attributes.get("gender");
    }
}
