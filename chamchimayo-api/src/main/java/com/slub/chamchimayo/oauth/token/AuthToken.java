package com.slub.chamchimayo.oauth.token;

import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;

@ToString
@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    private static final String AUTHORITIES_KEY = "role";

    @Getter
    private final String token;
    private final Key key;

    AuthToken(String id, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, expiry);
    }

    AuthToken(String id, String role, Date expiry, Key key) {
        this.key = key;
        this.token = createAuthToken(id, role, expiry);
    }

    private String createAuthToken(String id, Date expiry) {
        return Jwts.builder()
            .setSubject(id)
            .signWith(key, SignatureAlgorithm.HS256)
            .setExpiration(expiry)
            .compact();
    }

    private String createAuthToken(String id, String role, Date expiry) {
        return Jwts.builder()
            .setSubject(id)
            .claim(AUTHORITIES_KEY, role)
            .signWith(key, SignatureAlgorithm.HS256)
            .setExpiration(expiry)
            .compact();
    }

    public boolean validate() {
        return this.getTokenClaims() != null;
    }

    public Claims getTokenClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)// JSON Web Signature
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature. 잘못된 JWT 서명");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token. 유효하지 않은 구성의 JWT 토큰");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. 만료된 JWT 토큰");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token. 지원되지 않는 형식이거나 구성의 JWT 토큰");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid, 잘못된 JWT");
        }
        return null;
    }

    public Claims getExpiredTokenClaims() {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
            return e.getClaims();
        }
        return null;
    }
}
