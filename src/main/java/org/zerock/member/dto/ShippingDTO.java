package org.zerock.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingDTO<E> {

    private Long dno;

    @Column(length = 100)
    private String mid;         // 회원 DB와 연결

    @NotEmpty
    private String dname;       // 배송지명

    @NotEmpty
    private String dperson;     // 받는 분

    @NotEmpty
    private String zonecode;    // 우편번호

    @NotEmpty
    private String address;     // 주소

    @NotEmpty
    private String daddress;    // 상세 주소

    @NotEmpty
    private String dphonenum;   // 휴대폰 번호

    private String ddefault;   // 기본배송지

    private List<E> dtoList;
}
