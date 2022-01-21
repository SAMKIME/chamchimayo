package com.slub.chamchimayo.dto;

import com.slub.chamchimayo.entity.User;
import java.io.Serializable;
import lombok.Getter;

/**
 * 직렬화 기능을 가진 User 클래스
 */
@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
