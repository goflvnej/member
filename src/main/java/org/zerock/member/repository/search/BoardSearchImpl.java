package org.zerock.member.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.member.dto.BoardListReplyCountDTO;
import org.zerock.member.entity.Board;
import org.zerock.member.entity.QBoard;
import org.zerock.member.entity.QReply;

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

    // 특정 게시글에 대한 댓글 연결 및 페이징 처리
    @Override
    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {

        // 하나의 엔티티만으로 접근할 수 없는 정보에 접근하기 위해 JPQL로 조인 이용
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);

        // 게시글과 댓글 중 한쪽에만 데이터가 있을 수 있으므로 left join(outer join)이나 inner join 이용
        query.leftJoin(reply).on(reply.board.eq(board));

        // group by로 게시글 당 처리
        query.groupBy(board);

        // 검색 조건
        if((types != null && types.length > 0) && keyword != null) {

            // BooleanBuilder : Querydsl에서 WHERE 조건이 필요할 때 사용
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type : types) {

                switch(type) {
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

        // bno가 0보다 큰 조건 설정 -> bno를 인덱스로 사용
        query.where(board.bno.gt(0L));

        // Projection : JPQLQuery의 select()를 이용해서 JPQL의 결과를 바로 DTO로 처리
        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.bean(BoardListReplyCountDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")));

        // 쿼리문에 페이징 적용
        this.getQuerydsl().applyPagination(pageable, dtoQuery);

        // JPQLQuery 타입의 dtoQuery 객체를 제네릭 List 타입의 dtoList 객체로 변환
        List<BoardListReplyCountDTO> dtoList = dtoQuery.fetch();

        // fetchCount() : 작성한 SELECT 문을 바탕으로 COUNT 쿼리를 만들어서 실행
        long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList, pageable, count);
    }
}