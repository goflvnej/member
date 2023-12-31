package org.zerock.member.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.member.security.CustomUserDetailsService;
import org.zerock.member.security.handler.Custom403Handler;
import org.zerock.member.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 경로에 접근할 수 있는 권한 설정 -> @PreAuthorize, @PostAuthorize
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

    // 패스워드 암호화 (필수)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 모든 사용자가 모든 경로에 접근 가능
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("SecurityConfig.filterChain() 로그인 시 실행");

        http.formLogin()
                .loginPage("/member/login")                 // Form 로그인 기능 작동, 커스텀 로그인 페이지
                .defaultSuccessUrl("/mypage/read");         // 로그인 성공 시 리다이렉트 페이지
        http.csrf().disable();                              // CSRF 토큰 비활성화 (기본값은 GET 방식 제외 요구) -> USERNAME과 PASSWORD 만으로 로그인 가능

        // remember-me 쿠키의 값을 인코딩하기 위한 키와 정보를 저장할 때 사용하는 tokenRepository 지정
        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60*60*24*30);         // 유효 기간 30일

        // 403 에러 발생시 예외 처리로 로그인 페이지로 이동하고 파라미터에 error=ACCESS_DENIED 값 전달
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());    

        // OAuth2를 사용한 소셜 로그인
        http.oauth2Login()
                .loginPage("/member/login")
                .successHandler(authenticationSuccessHandler());

        return http.build();
    }

    // Custom403Handler 클래스 생성자 빈 처리
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    // css나 js 파일 등 정적 자원에 시큐리티 필터 적용 해제
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("SecurityConfig.webSecurityCustomizer() 정적 파일 시큐리티 적용 해제");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // remember-me 쿠키를 이용해서 로그인 정보 유지, (persistent_logins) 테이블 이용
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);

        return repo;
    }

    // 시큐리티 설정에 소셜 로그인 성공 시 페이지 이동 핸들러 추가
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {

        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }


}