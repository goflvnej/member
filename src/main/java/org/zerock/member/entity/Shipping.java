package org.zerock.member.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "shipping")
public class Shipping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;           // 배송 DB 기본키

    @Column(length = 100)
    private String mid;         // 회원 DB와 연결

    private String dname;       // 배송지명

    private String dperson;     // 받는 분

    private String zonecode;    // 우편번호

    @Column(length = 1000)
    private String address;     // 주소

    @Column(length = 1000)
    private String daddress;    // 상세 주소

    @Column(length = 20)
    private String dphonenum;   // 휴대폰 번호

    @Column(length = 10)
    private String ddefault;   // 기본배송지

    // 배송지명 수정
    public void change(String dname, String zonecode, String address, String daddress, String dphonenum, String ddefault){
        this.dname = dname;
        this.zonecode = zonecode;
        this.address = address;
        this.daddress = daddress;
        this.dphonenum = dphonenum;
        this.ddefault = ddefault;
    }

}
