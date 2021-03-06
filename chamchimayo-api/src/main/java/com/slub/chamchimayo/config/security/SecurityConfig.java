package com.slub.chamchimayo.config.security;

import com.slub.chamchimayo.config.properties.AppProperties;
import com.slub.chamchimayo.config.properties.CorsProperties;
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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
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
        http
            .cors()
            .and()
                // stateless??? ?????? ?????? ??????
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                // ?????? ???????????? ?????? CSRF ???????????? ????????????
                .csrf().disable()
                // ??? ?????? ?????? ????????????
                .formLogin().disable()
                // HTTP ?????? ?????? ????????????
                .httpBasic().disable()
                // ?????? ?????? ?????? ??? ????????? ?????? ????????? ??????
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(tokenAccessDeniedHandler)
            .and()
                // ????????? ??? ?????? ?????? ??????
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(PUBLIC_URI).permitAll()
//                .antMatchers("/api/**").hasAnyAuthority(RoleType.USER.getCode()) // api/** ??? USER????????? ?????? ??????
//                .antMatchers("/api/**/admin/**").hasAnyAuthority(RoleType.ADMIN.getCode()) ///api/**/admin/**??? admin??? ?????? ??????
                .anyRequest().authenticated()
            .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
            .and()
                .redirectionEndpoint()
                .baseUri("/*/oauth2/callback/*")
            .and()
                .userInfoEndpoint() // oauth2 ????????? ????????? ????????? ?????? ?????????
                .userService(oAuth2UserService) // ?????? ????????? ?????? ??? ?????? ????????? ????????? UserService ???????????? ????????? ??????
                                                // ????????? ???????????? ????????? ????????? ????????? ???????????? ????????? ??????????????? ?????? ?????? ??????
            .and()
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler());

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * UserDetailsService ??????
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    /**
     * auth ????????? ??????
     */
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * security ?????? ???, ????????? ????????? ??????
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * ?????? ?????? ??????
     */
    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService);
    }

    /**
     * ?????? ?????? ?????? Repository
     * ?????? ????????? ???????????? ????????? ??? ??????
     */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /**
     * OAuth ?????? ?????? ?????????
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
     * OAuth ?????? ?????? ?????????
     */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }

    /**
     * Cors ??????
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
