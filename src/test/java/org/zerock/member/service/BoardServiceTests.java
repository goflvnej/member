package org.zerock.member.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("register 테스트 제목")
                .content("register 테스트 내용")
                .writer("user0")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);
    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(98L)
                .title("modify 테스트 제목 98")
                .content("modify 테스트 내용 98")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tcw")
                .keyword("1")
                .build();

        // 시작/끝 페이지, 이전/다음 페이지 여부, 전체 끝 페이지 생성
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);

        log.info(responseDTO);
    }
}
