package com.slub.chamchimayo.config;

import com.slub.chamchimayo.oauth.entity.RoleType;
import com.slub.chamchimayo.oauth.exception.RestAuthenticationEntryPoint;
import com.slub.chamchimayo.oauth.filter.TokenAuthenticationFilter;
import com.slub.chamchimayo.oauth.handler.OAuth2AuthenticationFailureHandler;
import com.slub.chamchimayo.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.slub.chamchimayo.oauth.handler.TokenAccessDeniedHandler;
import com.slub.chamchimayo.oauth.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.slub.chamchimayo.oauth.service.CustomOAuth2UserService;
import com.slub.chamchimayo.oauth.service.CustomUserDetailService;
import com.slub.chamchimayo.oauth.token.AuthTokenProvider;
import com.slub.chamchimayo.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsProperties corsProperties;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final CustomUserDetailService userDetailsService;
    private final CustomOAuth2UserService oAuth2UserService;
    private final TokenAccessDeniedHandler tokenAccessDeniedHandler;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private static final String[] PUBLIC_URI = { "/**"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
                // stateless한 세션 정책 설정
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // 개발 편의성을 위해 CSRF 프로텍션 비활성화
                .csrf().disable()
                // 폼 기반 인증 비활성화
                .formLogin().disable()
                // HTTP 기본 인증 비활성화
                .httpBasic().disable()
                // 인증 오류 발생 시 처리를 위한 핸들러 추가
                .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler)
            .and()
                // 리소스 별 허용 범위 설정
                .authorizeRequests()
                .antMatchers(PUBLIC_URI).permitAll()
//                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//                .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode()) // api/** 은 USER권한만 접근 가능
//                .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode()) ///api/**/admin/**은 admin만 접근 가능
                .anyRequest().authenticated()
            .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
            .and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/code/*")
            .and()
                .userInfoEndpoint() // oauth2 로그인 성공후 가져올 때의 설정들
                .userService(oAuth2UserService) // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페스 구현체 등록
                                                // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
            .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * UserDetailsService 설정
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    /**
     * auth 매니저 설정
     */
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * security 설정 시, 사용할 인코더 설정
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 토큰 필터 설정
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService);
    }

    /**
     * 쿠키 기반 인가 Repository
     * 인가 응답을 연계하고 검증할 때 사용
     */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /**
     * OAuth 인증 성공 핸들러
     */
    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(
            tokenProvider,
            appProperties,
            userRefreshTokenRepository,
            oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    /**
     * OAuth 인증 실패 핸들러
     */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /**
     * Cors 설정
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfig.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfig.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfig.setAllowCredentials(true);

        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }
}
