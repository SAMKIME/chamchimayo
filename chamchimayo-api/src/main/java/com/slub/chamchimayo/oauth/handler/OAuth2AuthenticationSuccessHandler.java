package com.slub.chamchimayo.oauth.handler;

import com.slub.chamchimayo.config.properties.AppProperties;
import com.slub.chamchimayo.entity.UserRefreshToken;
import com.slub.chamchimayo.oauth.entity.ProviderType;
import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfo;
import com.slub.chamchimayo.oauth.info.OAuth2UserInfoFactory;
import com.slub.chamchimayo.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.slub.chamchimayo.oauth.token.AuthToken;
import com.slub.chamchimayo.oauth.token.AuthTokenProvider;
import com.slub.chamchimayo.repository.UserRefreshTokenRepository;
import com.slub.chamchimayo.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static com.slub.chamchimayo.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;
import static com.slub.chamchimayo.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REFRESH_TOKEN;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider tokenProvider;
    private final AppProperties appProperties;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("????????? ?????? ?????????????????????. {} ??? ??????????????? ??? ????????????. " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                                        .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new IllegalArgumentException("???????????????! ???????????? ?????? ??????????????? uri??? ?????? ????????? ????????? ??? ????????????.");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        log.info("targetUrl : " + targetUrl);

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();

        RoleType roleType = hasAuthority(authorities, RoleType.ADMIN.getCode()) ? RoleType.ADMIN : RoleType.USER;

        // access token ??????
        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
            userInfo.getId(),
            roleType.getCode(),
            new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        // refresh token ??????
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = tokenProvider.createAuthToken(
            appProperties.getAuth().getTokenSecret(),
            new Date(now.getTime() + refreshTokenExpiry)
        );

        // refresh Token DB ??????
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(userInfo.getId());
        if (userRefreshToken != null) {
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        } else {
            userRefreshToken = new UserRefreshToken(userInfo.getId(), refreshToken.getToken());
            userRefreshTokenRepository.save(userRefreshToken);
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", accessToken.getToken())
            .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookie(request, response);
    }

    private boolean hasAuthority(Collection<? extends GrantedAuthority> authorities, String authority) {
        if (authorities == null) {
            return false;
        }

        for (GrantedAuthority grantedAuthority : authorities) {
            if (authority.equals(grantedAuthority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
            .stream()
            .anyMatch(authorizedRedirectUri -> {

                URI authorizedURI = URI.create(authorizedRedirectUri);
                log.info("authorizedURI : " + authorizedURI);
                if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                    && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                    return true;
                }
                return false;
            });
    }
}
