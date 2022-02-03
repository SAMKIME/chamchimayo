package com.slub.chamchimayo.entity;

import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.exception.ExceptionWithCodeAndMessage;
import javax.validation.constraints.NotNull;
import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {

    private static final int NAME_LENGTH_LIMIT = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private String userId;

    @NotNull
    @Column
    private String password;

    @NotNull
    @Column
    private String name;

    @NotNull
    @Column
    private String email;

    @Column
    private String gender;

    @Column
    private String mobile;

    @Column
    private String area;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider_type")
    private ProviderType providerType;

    @Builder
    public User(
        String userId,
        String name,
        String email,
        String gender,
        String mobile,
        String area,
        RoleType roleType,
        ProviderType providerType) {

        validateName(name);
        this.userId = userId;
        this.password = "NO_PASSWORD";
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.mobile = mobile;
        this.area = area;
        this.roleType = roleType;
        this.providerType = providerType;
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

    public User updateUser(String name, String gender) {
        this.name = name;
        this.gender = gender;
        return this;
    }

    public String getRoleKey(){
        return this.roleType.getCode();
    }
}
