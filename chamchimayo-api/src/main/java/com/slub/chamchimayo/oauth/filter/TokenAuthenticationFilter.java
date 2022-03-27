package com.slub.chamchimayo.oauth.filter;

import com.slub.chamchimayo.oauth.service.CustomUserDetailService;
import com.slub.chamchimayo.oauth.token.AuthToken;
import com.slub.chamchimayo.oauth.token.AuthTokenProvider;
import com.slub.chamchimayo.utils.HeaderUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        log.info("Token Filter 진입");

        String tokenStr = HeaderUtil.getAccessToken(request); // 헤더에 담긴 토큰 가져오기
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);

        if (token.validate()) { // 토큰 유효성 검사
            log.info("유효한 토큰.");

            String userId = tokenProvider.getUserIdFromToken(token);

            UserDetails userDetails = userDetailService.loadUserByUsername(userId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
