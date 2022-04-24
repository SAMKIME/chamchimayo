package com.slub.chamchimayo.controller;

import static com.slub.chamchimayo.exception.ExceptionWithCodeAndMessage.INVALID_ACCESS_TOKEN;
import static com.slub.chamchimayo.exception.ExceptionWithCodeAndMessage.INVALID_REFRESH_TOKEN;
import static com.slub.chamchimayo.exception.ExceptionWithCodeAndMessage.NOT_EXPIRED_TOKEN_YET;

import com.slub.chamchimayo.config.properties.AppProperties;
import com.slub.chamchimayo.dto.request.AuthReqModel;
import com.slub.chamchimayo.entity.UserRefreshToken;
import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.oauth.entity.UserPrincipal;
import com.slub.chamchimayo.oauth.token.AuthToken;
import com.slub.chamchimayo.oauth.token.AuthTokenProvider;
import com.slub.chamchimayo.repository.UserRefreshTokenRepository;
import com.slub.chamchimayo.utils.CookieUtil;
import com.slub.chamchimayo.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import javax.servlet.http.Cookie;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final static String REFRESH_TOKEN = "refresh_token";
    private final static long THREE_DAYS_MSEC = 259200000;

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    // id, pw로 로그인
    @PostMapping("/login")
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response, @RequestBody AuthReqModel authReqModel) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authReqModel.getId(),
                authReqModel.getPassword()
            )
        );

        String userId = authReqModel.getId();

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
            userId,
            ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
            new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
            appProperties.getAuth().getTokenSecret(),
            new Date(now.getTime() + refreshTokenExpiry)
        );

        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userId);
        if (userRefreshToken == null) {
            // 없는 경우 새로등록
            userRefreshToken = new UserRefreshToken(userId, refreshToken.getToken());
            userRefreshTokenRepository.save(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return ResponseEntity.ok(accessToken.getToken());
    }

    /**
     * 토큰만료 확인 및 재발급
     */
    @GetMapping("/refresh")
    public ResponseEntity refreshToken (HttpServletRequest request, HttpServletResponse response) {
        // access token 확인
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);
        if (!authToken.validate()) {
            return new ResponseEntity<>(INVALID_ACCESS_TOKEN.findMessage(), HttpStatus.BAD_REQUEST);
        }

        // expired access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            return new ResponseEntity<>(NOT_EXPIRED_TOKEN_YET.findMessage(), HttpStatus.BAD_REQUEST);
        }

        String userId = claims.getSubject();
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token
        String refreshToken = CookieUtil.getCookie(request, REFRESH_TOKEN)
            .map(Cookie::getValue)
            .orElse((null));
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(refreshToken);

        if (authRefreshToken.validate()) {
            return new ResponseEntity<>(INVALID_REFRESH_TOKEN.findMessage(), HttpStatus.BAD_REQUEST);
        }

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserIdAndRefreshToken(userId, refreshToken);
        if (userRefreshToken == null) {
            return new ResponseEntity<>(INVALID_REFRESH_TOKEN.findMessage(), HttpStatus.BAD_REQUEST);
        }

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
            userId,
            roleType.getCode(),
            new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();

        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
            );

            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

            int cookieMaxAge = (int) refreshTokenExpiry / 60;
            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);
        }

        return ResponseEntity.ok(new Result<>("token", newAccessToken));
    }

    @Getter
    @Setter
    static class Result<T> {
        private String name;
        private  T data;

        public Result(String name, T data) {
            this.name = name;
            this.data = data;
        }
    }
}