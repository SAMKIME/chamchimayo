package com.slub.chamchimayo.dto.request;

import com.slub.chamchimayo.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateRequest {

    private String name;
    private String email;
    private String gender;
    private String mobile;
    private String area;
    private String profileImageUrl;

    @Builder
    public UserUpdateRequest(String name, String email, String gender, String mobile, String area, String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.area = area;
        this.profileImageUrl = profileImageUrl;
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .gender(gender)
                .email(email)
                .mobile(mobile)
                .area(area)
                .profileImage(profileImageUrl)
                .build();
    }
}
