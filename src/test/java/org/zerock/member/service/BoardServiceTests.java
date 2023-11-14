package org.zerock.member.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.member.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
/*
    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(98L)
                .title("modify 테스트 제목 98")
                .content("modify 테스트 내용 98")
                .build();

        boardService.modify(boardDTO);
    }*/

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

    @Test
    public void testRegisterWithImages() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("게시글 테스트 첨부파일 포함")
                .content("테스트 내용")
                .writer("member100")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID() + "_aaa.jpg",
                        UUID.randomUUID() + "_bbb.jpg",
                        UUID.randomUUID() + "_ccc.jpg"
                )
        );

        Long bno = boardService.register(boardDTO);

        log.info("bno : " + bno);
    }

    @Test
    public void testReadAll() {

        Long bno = 102L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info("boardDTO : " + boardDTO);

        for(String fileName : boardDTO.getFileNames()) {
            log.info("fileName : " + fileName);
        }
    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(102L)
                .title("게시글 제목 수정")
                .content("첨부파일 수정 포함")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID() + "_zzz.jpg"));

        boardService.modify(boardDTO);
    }

    @Test
    public void testRemoveAll() {

        Long bno = 99L;

        boardService.remove(bno);
    }

    @Test
    public void testListWithAll() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("t")
                .keyword("테스트")
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info("Bno : " + boardListAllDTO.getBno() + ", Title : " + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null) {
                for(BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages()) {
                    log.info(boardImageDTO);
                }
            }

            log.info("-----------------------------------");
        });
    }
}
