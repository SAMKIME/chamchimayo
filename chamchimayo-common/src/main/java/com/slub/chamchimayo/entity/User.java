package com.slub.chamchimayo.entity;

import com.slub.chamchimayo.entity.enums.Role;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String gender;

    @Column
    private String mobile;

    @Column
    private String area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String name, String gender, String email, String mobile, String area, Role role) {
        validateName(name);
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.area = area;
        this.role = role;
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

    public User update(String name){
        this.name = name;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
