package org.zerock.member.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.member.dto.BoardListAllDTO;
import org.zerock.member.dto.BoardListReplyCountDTO;
import org.zerock.member.entity.Board;

// Querydsl을 사용할 인터페이스 선언
public interface BoardSearch {

    // 게시글 제목에 1이 들어가는 게시글 페이지 처리 -> 페이징 처리 시 실행되는 쿼리문에 limit 포함되는지 확인해야 함
    Page<Board> search1(Pageable pageable);

    // 검색 조건과 키워드
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

    // 특정 게시글의 댓글 개수 및 페이징
    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

    // 모든 게시글과 각 게시글당 이미지 페이징 후 댓글 개수와 함께 조회
    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);

}
