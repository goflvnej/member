package org.zerock.member.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.member.domain.Board;
import org.zerock.member.domain.QBoard;

import java.util.List;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    public BoardSearchImpl() {
        super(Board.class);
    }

    // 제목에 "1"이 포함된 게시글 페이징 처리 검색
    @Override
    public Page<Board> search1(Pageable pageable) {

        QBoard board = QBoard.board;            // Q도메인 객체
        JPQLQuery<Board> query = from(board);   // SELECT ... FROM board
        query.where(board.title.contains("1")); // WHERE TITLE LIKE '%1%'
        
        // 페이징 처리 적용
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();       // fetch() : JPQLQuery 실행
        long count = query.fetchCount();        // fetchCount() : count 쿼리 실행

        return null;
    }

    // 검색 조건과 키워드를 활용한 페이징 처리 검색
    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);

        // 검색 조건과 키워드가 있을 때
        if( (types != null && types.length > 0 ) && keyword != null ) {

            // BooleanBuilder : Querydsl을 사용할 때 ()가 필요할 때 사용
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        // bno > 0 조건 추가
        query.where(board.bno.gt(0L));

        // 페이징 처리
        this.getQuerydsl().applyPagination(pageable, query);
        
        List<Board> list = query.fetch();
        long count = query.fetchCount();

        // 쿼리문, 페이징 처리(현재 페이지, 한 페이지당 게시글 개수, 정렬), 결과 개수를 매개변수로 Page<Board> 객체 리턴
        return new PageImpl<>(list, pageable, count);
    }


}
