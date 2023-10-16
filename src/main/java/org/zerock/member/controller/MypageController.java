package org.zerock.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;
import org.zerock.member.dto.ShippingDTO;
import org.zerock.member.service.ShippingService;

import javax.validation.Valid;

@Controller
@RequestMapping("/mypage")
@Log4j2
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")  // 로그인한 사용자만 조회 가능
public class MypageController {

    private final ShippingService shippingService;

    // 배송지 관리 -> 모달창 폼을 띄워서 배송지 등록
    @GetMapping("/shipping")
    public void shippingGet(Model model) {

        log.info("MypageController.shippingGet() 배송지 관리 페이지");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String mid = userDetails.getUsername();

        // 배송 주소 전체 조회
        model.addAttribute("shippingDTO", shippingService.readAll(mid));

//        // 배송 모달창 1개 조회
//        model.addAttribute("dto", shippingService.readOne(dno));
    }

    // 배송지 관리 -> 모달창에 입력한 값 DB에 저장
    @PostMapping("/shipping")
    public String shippingPost(@Valid ShippingDTO shippingDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("BoardController.registerPost() 배송지 등록");

        // BindingResult는 파라미터에서 @Valid 객체 바로 뒤에 선언되어야 함
        // 잘못된 입력값을 보냈을 시 검증 대상 객체(target)와 검증 결과(errors)에 대한 정보를 담고있는 BindingResult 객체 생성
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/mypage/shipping";
        }

        log.info(shippingDTO);

        Long dno = shippingService.register(shippingDTO);
        redirectAttributes.addFlashAttribute("result", dno);

        return "redirect:/mypage/shipping";
    }
}
