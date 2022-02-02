package com.slub.chamchimayo.dto;

import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.entity.RoleType;
import java.io.Serializable;
import lombok.Getter;
import lombok.ToString;

/**
 * 직렬화 기능을 가진 User 클래스
 */
@Getter
@ToString
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String gender;
    private String mobile;
    private RoleType role;
    private ProviderType providerType;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.mobile = user.getMobile();
        this.role = user.getRoleType();
        this.providerType = user.getProviderType();
    }
}
