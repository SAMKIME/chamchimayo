package com.slub.chamchimayo.dto.response;

import com.slub.chamchimayo.entity.User;
import com.slub.chamchimayo.oauth.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String userId;
    private String name;
    private String gender;
    private String email;
    private String mobile;
    private String area;
    private RoleType roleType;

    public static UserResponse of(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .userId(user.getUserId())
            .name(user.getName())
            .gender(user.getGender())
            .email(user.getEmail())
            .mobile(user.getMobile())
            .area(user.getArea())
            .roleType(user.getRoleType())
            .build();
    }
}
