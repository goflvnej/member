package org.zerock.member.security.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.zerock.member.security.dto.MemberSecurityDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {
// 소설 로그인 후 마이페이지로 이동
    
    private final PasswordEncoder passwordEncoder;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("CustomSocialLoginSuccessHandler.onAuthenticationSuccess() 소설 로그인 성공 후에 페이지 이동 처리");
        log.info(authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getMpassword();

        // 소셜 로그인이고 회원 비밀번호가 1111인 경우
        if(memberSecurityDTO.isMsocial() &&
                (memberSecurityDTO.getMpassword().equals("1111") || passwordEncoder.matches("1111", memberSecurityDTO.getMpassword()))) {

            log.info("소설 로그인 후 마이페이지 화면으로 이동");
            response.sendRedirect("/mypage/read");

            return;
        } else {

            response.sendRedirect("/board/list");
        }
    }
}
