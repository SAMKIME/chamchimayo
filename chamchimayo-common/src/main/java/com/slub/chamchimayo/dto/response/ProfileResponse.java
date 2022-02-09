package com.slub.chamchimayo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProfileResponse {

    private String profileImage;

    public static ProfileResponse of(String imageUrl) {
        return new ProfileResponse(imageUrl);
    }
}
