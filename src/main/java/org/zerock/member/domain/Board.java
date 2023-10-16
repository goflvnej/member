package org.zerock.member.domain;

import lombok.*;

import javax.persistence.*;

@Entity // @Id 필수
@Getter
@Builder
@AllArgsConstructor // @Builder 어노테이션 사용 시 필수 적용
@NoArgsConstructor  // @Builder 어노테이션 사용 시 필수 적용
@ToString
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;
    // IDENTITY : 키 생성 전략을 데이터베이스에 위임(MySQL, MariaDB)
    // SEQUENCE : 데이터베이스 시퀀스 오브젝트 사용(오라클), @SequenceGenerator 필요
    // TABLE : 키 생성용 테이블 사용, @TableGenerator 필요
    // AUTO : 자동 지정, 기본값

    @Column(length = 500, nullable = false)
    private String title;

    @Column(length = 2000, nullable = false)
    private String content;

    @Column(length = 50, nullable = false)
    private String writer;

    // setter 역할을 하는 change 메서드 -> BoardRepositoryTests에서 update 테스트
    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
