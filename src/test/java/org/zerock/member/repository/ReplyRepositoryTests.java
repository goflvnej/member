package org.zerock.member.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.member.entity.Board;
import org.zerock.member.entity.Reply;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {

        // 실제 DB에 있는 bno 사용
        Long bno = 100L;

        IntStream.rangeClosed(1,100).forEach(i -> {

            Board board = Board.builder()
                    .bno(bno)
                    .build();

            // Reply 클래스의 @Builder 어노테이션 사용
            Reply reply = Reply.builder()
                    .board(board)
                    .replyText("댓글 테스트 " + i)
                    .replyer("member100")
                    .build();

            // save()는 해당 엔티티 객체가 없으면 insert, 있으면 update를 실행
            replyRepository.save(reply);
        });
    }

    @Test
    public void testBoardReplies() {

        Long bno = 100L;

        // 첫 번째 페이지에 10개를 rno(댓글 번호) 내림차순으로 정렬 페이징 처리
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }
}
