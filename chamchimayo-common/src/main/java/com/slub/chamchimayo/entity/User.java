package com.slub.chamchimayo.entity;

import com.slub.chamchimayo.exception.ExceptionWithCodeAndMessage;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Entity
public class User {

    private static final int NAME_LENGTH_LIMIT = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String gender;

    @Column(nullable = false)
    private String email;

    @Column
    private String mobile;

    @Column
    private String area;

    private String profileImage;

    @Builder
    public User(String name, String gender, String email, String mobile, String area, String profileImage) {
        validateName(name);
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.area = area;
        this.profileImage = profileImage;
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > NAME_LENGTH_LIMIT) {
            throw ExceptionWithCodeAndMessage.INVALID_INPUT_LENGTH.getException();
        }
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeMobile(String mobile) {
        this.mobile = mobile;
    }

    public void changeArea(String area) {
        this.area = area;
    }
}
