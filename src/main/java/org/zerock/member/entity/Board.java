package org.zerock.member.entity;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // @Id 필수
@Getter
@Builder
@AllArgsConstructor // @Builder 어노테이션 사용 시 필수 적용
@NoArgsConstructor  // @Builder 어노테이션 사용 시 필수 적용
@ToString(exclude = "imageSet")
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

    // @OneToMany : PK를 가진 쪽에서 사용 (Board는 참조하는 클래스이다.) -> board 테이블과 board_image 테이블 사이에 연관관계를 매핑해주는 board_image_set 테이블 생성
    // mappedBy : 어떤 엔티티의 속성으로 매핑되는지를 의미함, 매핑 테이블을 생성하지 않는 다른 방법으로는 @JoinColumn을 이용할 수 있음 -> BoardImage의 board 변수
    // CascadeType.ALL : Board 엔티티 객체의 모든 변화에 대해 BoardImage 객체도 같이 자동으로 변경됨 -> 같이 관리되는 경우 별도로 BoardImageRepository 생성 안 함
    @OneToMany(mappedBy = "board",          // BoardImage의 board 변수와 연결되어 있음
               cascade = {CascadeType.ALL}, // Board 엔티티의 모든 상태 변화에 대해 BoardImage 객체도 함께 변경됨
               fetch = FetchType.LAZY,
               orphanRemoval = true)        // 하위 속성값이 true인 경우 하위 엔티티에도 실제로 삭제가 이루어짐 -> 게시글 엔티티 삭제 시 함께 삭제됨
    @Builder.Default                        // 빌더 패턴 사용 시 기본값 지정
    @BatchSize(size = 10)                   // N+1문제 해결책 : 모든 게시글에 대해 이미지셋을 조회하는 쿼리가 매번 발생하는 문제 해결 (게시글 10개씩 묶어서 imageSet 쿼리문 실행)
    private Set<BoardImage> imageSet = new HashSet<>();

    // 게시글 이미지 추가
    public void addImage(String uuid, String fileName) {

        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        
        imageSet.add(boardImage);
    }

    // 게시글 이미지 삭제
    public void clearImages() {

        imageSet.forEach(boardImage -> boardImage.changeBoard(null));

        this.imageSet.clear();
    }
}
