package org.zerock.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member.domain.Member;
import org.zerock.member.dto.MemberJoinDTO;
import org.zerock.member.repository.MemberRepository;
import org.zerock.member.security.dto.MemberSecurityDTO;
import org.zerock.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // 회원정보 조회
    @PreAuthorize("isAuthenticated()")  // 로그인한 사용자만 조회 가능
    @GetMapping({"/mypage/read", "/mypage/modify"})
    public void read(Principal principal, Model model) {

        log.info("MemberController.read() 회원정보 읽기 페이지");
        log.info("유저 아이디 : " + principal.getName());

        String mid = principal.getName();
        MemberSecurityDTO memberSecurityDTO = memberService.readOne(mid);

        model.addAttribute("dto", memberSecurityDTO);
    }

    // 회원정보 수정
    @PostMapping("/mypage/modify")
    public String modify(@Valid MemberSecurityDTO memberSecurityDTO, Model model, RedirectAttributes redirectAttributes) {

        model.addAttribute("dto", memberSecurityDTO);

        log.info("MemberController.modify() 회원정보 수정 페이지");
        log.info("memberSecurityDTO 아이디, 비밀번호, 이름");
        log.info(memberSecurityDTO.getMid(), memberSecurityDTO.getMpassword(), memberSecurityDTO.getMname());

        redirectAttributes.addFlashAttribute("result", "modified");
        memberService.modify(memberSecurityDTO);

        return "redirect:/member/mypage/read";

    }
}
