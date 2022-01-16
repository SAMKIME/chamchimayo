package com.slub.chamchimayo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    private String name;
    private String email;
    private String gender;
    private String mobile;
    private String area;
}
