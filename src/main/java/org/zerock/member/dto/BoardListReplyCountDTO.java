package org.zerock.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
// 목록 화면에서 특정 게시글의 댓글 수를 같이 출력하기 위한 DTO

    private Long bno;               // 게시글 번호

    private String title;           // 게시글 제목

    private String writer;          // 게시글 작성자

    private LocalDateTime regDate;  // 게시글 등록일

    private Long replyCount;        // 댓글 개수
}
