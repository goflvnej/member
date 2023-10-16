package org.zerock.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.member.domain.Board;
import org.zerock.member.repository.search.BoardSearch;

// JpaRepository 엔티티 타입 : Board, @Id 타입 : Long
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    // nativeQuery는 JPOL이 아닌 일반 쿼리문을 사용
    // 엔티티 속성을 반영하지 않아 특정 DB에 의존성을 가짐
    @Query(value = "SELECT now()", nativeQuery = true)
    String getTime();
}
