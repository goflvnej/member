package org.zerock.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.member.entity.Board;
import org.zerock.member.repository.search.BoardSearch;

import java.util.Optional;

// JpaRepository 엔티티 타입 : Board, @Id 타입 : Long
public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {

    // nativeQuery는 JPQL이 아닌 일반 쿼리문을 사용
    // 엔티티 속성을 반영하지 않아 특정 DB에 의존성을 가짐
    @Query(value = "SELECT now()", nativeQuery = true)
    String getTime();

    @EntityGraph(attributePaths = {"imageSet"})         // 같이 로딩 되어야 하는 속성 명시
    @Query("SELECT b FROM Board b WHERE b.bno = :bno")
    Optional<Board> findByIdWithImages(Long bno);
}
