package org.zerock.member.service;

import org.zerock.member.dto.PageRequestDTO;
import org.zerock.member.dto.PageResponseDTO;
import org.zerock.member.dto.ReplyDTO;

public interface ReplyService {

    // 등록
    Long register(ReplyDTO replyDTO);

    // 조회
    ReplyDTO read(Long rno);

    // 수정
    void modify(ReplyDTO replyDTO);

    // 삭제
    void remove(Long rno);

    // 페이징 처리
    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
