package org.zerock.member.service;

import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;

public interface BoardService {

    // 등록
    Long register(BoardDTO boardDTO);

    // 조회
    BoardDTO readOne(Long bno);

    // 수정
    void modify(BoardDTO boardDTO);

    // 삭제
    void remove(Long bno);

    // 목록/검색
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
}
