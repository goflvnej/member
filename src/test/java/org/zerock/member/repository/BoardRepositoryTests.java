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

import java.util.List;
import java.util.Optional;
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
                    .writer("user" + (i % 10))
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
}