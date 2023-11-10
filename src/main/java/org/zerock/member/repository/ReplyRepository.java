package org.zerock.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.member.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 댓글에 페이징 처리 기능 추가
    @Query("SELECT r FROM Reply r WHERE r.board.bno = :bno")
    Page<Reply> listOfBoard(Long bno, Pageable pageable);

    // 게시글 삭제 시 해당 게시글의 댓글 삭제
    void deleteByBoard_Bno(Long bno);
}
