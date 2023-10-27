package org.zerock.member.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage> {
// Comparable 인터페이스를 사용하여 @OneToMany 처리에서 순번에 맞게 정렬

    @Id
    private String uuid;        // 고유 UUID

    private String fileName;    // 파일명

    private int ord;            // 파일 순번

    @ManyToOne                  // FK를 가진 쪽에서 사용 (BoardImage는 참조 받는 클래스이다.)
    private Board board;

    @Override
    public int compareTo(BoardImage other) {
        return this.ord - other.ord;
    }

    public void changeBoard(Board board) {
        this.board = board;
    }
}
