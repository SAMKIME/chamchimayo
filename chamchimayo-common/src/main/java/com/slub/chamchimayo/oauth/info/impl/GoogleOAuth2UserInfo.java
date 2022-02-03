package com.slub.chamchimayo.oauth.info.impl;

import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
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

    @Override
    public User toEntity() {
        return User.builder()
            .name(getName())
            .email(getEmail())
            .gender(getGender())
            .providerType(ProviderType.GOOGLE)
            .roleType(RoleType.USER) //기본 권한 USER
            .snsId(getId())
            .build();
    }
}
