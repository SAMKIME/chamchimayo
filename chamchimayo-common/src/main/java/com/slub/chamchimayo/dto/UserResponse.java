package com.slub.chamchimayo.dto;

import com.slub.chamchimayo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String gender;
    private String email;
    private String mobile;
    private String area;

    public static UserResponse of(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getGender(), user.getEmail(),
                user.getMobile(), user.getArea());
    }
}
