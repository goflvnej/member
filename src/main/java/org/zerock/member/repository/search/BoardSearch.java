package org.zerock.member.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.member.entity.Board;

// Querydsl을 사용할 인터페이스 선언
public interface BoardSearch {

    // 페이지 처리
    Page<Board> search1(Pageable pageable);

    // 검색 조건과 키워드
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
}
