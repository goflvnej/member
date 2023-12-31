package org.zerock.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member.dto.MemberJoinDTO;
import org.zerock.member.security.dto.MemberSecurityDTO;
import org.zerock.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 로그인
    @GetMapping("/login")
    public void loginGet(String errorCode, String logout) {

        log.info("MemberController.loginGet() 로그인 처리");
        log.info("logout : " + logout);

        if(logout != null) {
            log.info("사용자 로그아웃");
        }
    }

    // 회원가입
    @GetMapping("/join")
    public void joinGet() {

        log.info("MemberController.joinGet() 회원가입 페이지 접근");
    }
    
    @PostMapping("/join")
    public String joinPost(MemberJoinDTO memberJoinDTO, RedirectAttributes redirectAttributes) {
        
        log.info("MemberController.joinPost() 회원가입 데이터 전송");
        log.info(memberJoinDTO);

        try {
            memberService.join(memberJoinDTO);

        } catch (MemberService.MidExistException e) {

            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/member/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");

        return "redirect:/board/list";
    }


}
