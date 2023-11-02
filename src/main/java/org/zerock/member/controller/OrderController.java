package org.zerock.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.member.security.dto.MemberSecurityDTO;
import org.zerock.member.service.MemberService;
import org.zerock.member.service.ShippingService;

import java.security.Principal;

@Controller
@RequestMapping("/order")
@Log4j2
@RequiredArgsConstructor //자동 주입을 위한 Annotation
@PreAuthorize("isAuthenticated()")  // 로그인한 사용자만 조회 가능
public class OrderController {

    private final MemberService memberService;

    private final ShippingService shippingService;

    // 주문서 작성
    @GetMapping("/checkout")
    public void info(Principal principal, Model model) {

        log.info("OrderController.info() 주문서 작성 페이지");

        // 주문자 정보 조회
        String mid = principal.getName();

        MemberSecurityDTO memberSecurityDTO = memberService.readOne(mid);

        model.addAttribute("memberDTO", memberSecurityDTO);
        
        // 배송지 정보 전체 조회
        model.addAttribute("shippingDTOList", shippingService.readAll(mid));

        // 배송지 정보 1개 조회
//        model.addAttribute("shippingDTO", shippingService.readOne(dno));

    }
}
