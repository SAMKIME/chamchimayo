package com.slub.chamchimayo.entity.oauth.info;

import java.util.Map;
import lombok.Getter;

@Getter
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes; // OAuth2 반환하는 유저 정보 MAP

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getGender();
}
