package com.slub.chamchimayo.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Builder
    public Member(String name, String phoneNumber, String mail) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String mail;

    public void setNameAndPhoneNumber(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
