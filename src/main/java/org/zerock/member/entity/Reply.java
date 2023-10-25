package org.zerock.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")            // 참조하는 객체에 대해 exclude 속성값 필수 (제거하면 board 테이블까지 쿼리 실행)
@Table(name = "Reply", indexes = {      // 쿼리 조건으로 자주 사용되는 게시물 번호에 인덱스 지정
        @Index(name = "idx_reply_board_bno", columnList = "board_bno")
})
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @ManyToOne(fetch = FetchType.LAZY)  // 다대일 관계 : 연관 관계에서 fetch 속성은 반드시 LAZY로 지정
    private Board board;

    private String replyText;

    private String replyer;

    // 댓글을 수정하는 경우 Reply 객체에서 replyText만 수정 가능
    public void changeText(String text) {
        this.replyText = text;
    }
}
