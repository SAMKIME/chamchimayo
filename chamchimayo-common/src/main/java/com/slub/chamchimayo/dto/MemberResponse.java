package com.slub.chamchimayo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private String mail;
    private String name;
    private String phoneNumber;

    @Builder
    public MemberResponse(String mail, String name, String phoneNumber) {
        this.mail = mail;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
