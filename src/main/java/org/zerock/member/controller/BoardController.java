package org.zerock.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.BoardListReplyCountDTO;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;
import org.zerock.member.service.BoardService;

import javax.validation.Valid;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {

//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        // 댓글을 포함한 게시글 목록
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);
    }

    @PreAuthorize("hasRole('USER')")    // 경로에 접근할 때 USER 권한을 사전에 체크
    @GetMapping("/register")
    public void registerGet() {

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("BoardController.registerPost() 게시글 등록");

        // BindingResult는 파라미터에서 @Valid 객체 바로 뒤에 선언되어야 함
        // 잘못된 입력값을 보냈을 시 검증 대상 객체(target)와 검증 결과(errors)에 대한 정보를 담고있는 BindingResult 객체 생성
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    @PreAuthorize("isAuthenticated()")  // 로그인한 사용자만 조회 가능
    @GetMapping({"/read", "/modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model) {

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PreAuthorize("principal.username == #boardDTO.writer") // 현재 로그인한 사용자의 아이디가 파라미터가 수집된 BoardDTO의 작성자와 일치할 때
    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO, BindingResult bindingResult, PageRequestDTO pageRequestDTO, RedirectAttributes redirectAttributes) {

        log.info("BoardController.modify() 게시글 수정 " + boardDTO);

        if(bindingResult.hasErrors()) {
            log.info("BoardController.modify() 예외 발생");

            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());

            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());

        return "redirect:/board/read";
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {

        log.info("BoardController.remove() 게시글 삭제 " + bno);

        boardService.remove(bno);
        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";
    }
}
