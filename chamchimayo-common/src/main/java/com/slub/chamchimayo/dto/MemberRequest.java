package com.slub.chamchimayo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRequest {

    private String mail;
    private String name;
    private String phoneNumber;

    @Builder
    public MemberRequest(String name, String phoneNumber, String mail) {
        this.mail = mail;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
