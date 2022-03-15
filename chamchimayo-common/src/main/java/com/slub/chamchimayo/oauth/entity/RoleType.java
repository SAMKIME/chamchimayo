package com.slub.chamchimayo.oauth.entity;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String code;
    private final String displayName;

    public static RoleType of(String code) {
        return Arrays.stream(RoleType.values())
            .filter(r -> r.getCode().equals(code))
            .findAny()
            .orElse(GUEST);
    }
}
