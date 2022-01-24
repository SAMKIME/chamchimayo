package com.slub.chamchimayo.config.oauth;

import com.slub.chamchimayo.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉토리의 하위 파일 목록은 인증 무시(항상 통과)
        web.ignoring().antMatchers( "/resources/**", "/css/**", "/images/**", "/js/**", "/lib/**", "/profile");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()// URL별 권한 권리
                .antMatchers("/**").permitAll()
                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // /api/v1/** 은 USER권한만 접근 가능
//                .anyRequest().authenticated()
            .and()
                .oauth2Login()
                    .userInfoEndpoint()// oauth2 로그인 성공 후 가져올 때의 설정들
                // 소셜로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
                        .userService(customOAuth2UserService)// 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
            .and()
                .defaultSuccessUrl("/loginSuccess")
                .failureUrl("/loginFailure")
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"));
    }
}
