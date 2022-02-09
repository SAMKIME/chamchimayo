package com.slub.chamchimayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private String profileImage;

    public static ProfileResponse of (String profileImage) {
        return new ProfileResponse(profileImage);
    }

}
