package com.slub.chamchimayo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Entity
@NoArgsConstructor
@Table(name = "USER_REFRESH_TOKEN")
public class UserRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_seq")
    private Long refreshTokenSeq;

    @NotNull
    @Column(name = "user_id", unique = true)
    private String userId;

    @Setter
    @NotNull
    @Column(name = "refresh_token")
    private String refreshToken;

    @Builder
    public UserRefreshToken(
        String userId,
        String refreshToken
    ) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
