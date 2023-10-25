package org.zerock.member.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;
import org.zerock.member.dto.ReplyDTO;
import org.zerock.member.service.ReplyService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController                 // 모든 메서드의 리턴 값이 JSP나 Thymeleaf가 아닌 JSON이나 XML로 처리됨
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor        // 의존성 주입
public class ReplyController {

    private final ReplyService replyService;

    // 특정 게시글의 댓글 조회
    @ApiOperation(value = "Replies of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")
    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
        // @PathVariable는 매개변수의 값을 직접 URL의 변수로 처리할 수 있게 함

        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);

        return responseDTO;
    }

    // 특정 댓글 조회
    @ApiOperation(value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping("/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {

        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;

    }

    // 등록
    @ApiOperation(value = "Replies POST", notes = "POST 방식으로 댓글 등록")        // Swagger UI에서 기능 설명할 때 사용
    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)    // consumes 속성은 해당 메서드를 받아서 JSON 데이터로 처리함을 명시
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult) throws BindException {
        // @RequestBody는 JSON 문자열을 ReplyDTO로 변환하기 위해 사용
        // @Valid 어노테이션을 붙여도 외래키 제약조건(bno)을 위반하면 예외 발생

        log.info("입력한 댓글 replyDTO : " + replyDTO);

        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();

        Long rno = replyService.register(replyDTO);

        resultMap.put("rno", rno);

        return resultMap;
    }

    // 삭제
    @ApiOperation(value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")
    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {

        replyService.remove(rno);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }

    // 수정
    @ApiOperation(value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify(@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {

        replyDTO.setRno(rno);   // rno로 댓글을 특정지음

        replyService.modify(replyDTO);

        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno", rno);

        return resultMap;
    }

}
