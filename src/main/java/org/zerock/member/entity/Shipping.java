package org.zerock.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
@Table(name = "shipping")
public class Shipping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sno;           // 배송 DB 기본키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid")
    private Member member;      // 회원 DB와 연결

    private String sname;       // 배송지명

    private String sperson;     // 받는 분

    private String zonecode;    // 우편번호

    @Column(length = 1000)
    private String address;     // 주소

    @Column(length = 1000)
    private String saddress;    // 상세 주소

    @Column(length = 10)
    private String sdefault;   // 기본배송지

    // 배송지명 수정
    public void change(String sname, String sperson, String zonecode, String address, String saddress, String sdefault){
        this.sname = sname;
        this.sperson = sperson;
        this.zonecode = zonecode;
        this.address = address;
        this.saddress = saddress;
        this.sdefault = sdefault;
    }

}
