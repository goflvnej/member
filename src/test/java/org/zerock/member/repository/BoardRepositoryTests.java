package org.zerock.member.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member.dto.BoardListReplyCountDTO;
import org.zerock.member.entity.Board;
import org.zerock.member.entity.BoardImage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            // Board 클래스의 @Builder 어노테이션 사용
            Board board = Board.builder()
                    .title("제목 " + i)
                    .content("내용  " + i)
                    .writer("member" + (i % 10))
                    .build();

            // save()는 해당 엔티티 객체가 없으면 insert, 있으면 update를 실행
            Board result = boardRepository.save(board);

            log.info("BNO : " + result.getBno());
        });
    }

    @Test
    public void testSelect() {
        Long bno = 100L;

        // Optional은 내부 값을 null 로 초기화한 싱글톤 객체를 Optional.empty() 메소드를 통해 제공한다.
        // null을 직접 리턴값으로 가져서 예외가 발생했을 때 프로그램을 종료시키는 것을 막기 위해 사용된다.
        // findById()는 특정한 번호의 게시글을 조회할 때 사용
        Optional<Board> result = boardRepository.findById(bno);

        // Optional에 담긴 객체가 null이 아니면 객체 반환, null이면 예외 발생
        Board board = result.orElseThrow();

        log.info(board);
    }

    @Test
    public void testUpdate() {
        Long bno = 99L;

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update 테스트 제목 " + bno,"update 테스트 내용 " + bno);

        boardRepository.save(board);
    }

    @Test
    public void testDelete() {
        Long bno = 101L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
        // 현재 페이지 번호 0부터 시작(pageNumber), 한 페이지당 게시글 개수 10개씩(pageSize), bno(게시글 번호) 내림차순 정렬
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("전체 게시글 개수 : " + result.getTotalElements());
        log.info("전체 페이지 번호 : " + result.getTotalPages());
        log.info("현재 페이지 번호 : " + result.getNumber());
        log.info("한 페이지당 게시글 개수 : " + result.getSize());

        List<Board> todoList = result.getContent();
        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1() {
        // 2페이지에 10개의 게시글을 번호 내림차순으로 정렬해서 페이징 처리
        Pageable pageable = PageRequest.of(1,10,Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {

        String[] types = {"t", "c", "W"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }

    @Test
    public void testSearchAll2() {

        String[] types = {"t", "c", "W"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info("전체 게시글 개수 : " + result.getTotalElements());
        log.info("전체 페이지 번호 : " + result.getTotalPages());
        log.info("현재 페이지 번호 : " + result.getNumber());
        log.info("한 페이지당 게시글 개수 : " + result.getSize());
        log.info("다음 페이지 유무 ? : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testSearchReplyCount() {

        // 제목, 내용, 작성자 모두 선택
        String[] types = {"t", "c", "w"};

        // 키워드 1
        String keyword = "9";

        // 페이징 처리 (0번째 페이지, 게시글 10개씩, bno (게시글 번호) 기준 내림차순
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info("전체 페이지 개수 " + result.getTotalPages());
        log.info("1페이지당 게시글 개수 " + result.getSize());
        log.info("현재 페이지 번호 " + result.getNumber());
        log.info("다음 페이지 존재 여부 " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

    @Test
    public void testInsertWithImages() {

        Board board = Board.builder()
                .title("게시글 첨부파일 이미지 테스트")
                .content("테스트")
                .writer("member100")
                .build();

        for (int i = 0; i < 3; i ++) {

            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
//    @Transactional : no session 오류를 해결하는 가장 쉬운 방법 -> BoardRepository에서 @EntityGraph 이용해도 됨
    public void testReadWithImages() {

        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        Board board = result.orElseThrow();

        log.info(board);

        for(BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages() {

        Optional<Board> result = boardRepository.findByIdWithImages(3L);
        Board board = result.orElseThrow();

        // 기존의 첨부파일들은 삭제
        board.clearImages();

        // 새로운 첨부파일들 추가
        for(int i = 0; i < 2; i ++) {

            board.addImage(UUID.randomUUID().toString(), "updatefile" + i + ".jpg");
        }

        boardRepository.save(board);
    }
}