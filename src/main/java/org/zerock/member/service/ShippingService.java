package org.zerock.member.service;

import org.zerock.member.dto.ShippingDTO;

import java.util.List;

public interface ShippingService {

    // 등록
    Long register(ShippingDTO ShippingDTO);

    // 조회 (아이디 별로 전체)
    List<ShippingDTO> readAll(String mid);

    // 조회 (해당 주소 1개) -> 사용 안 함
    ShippingDTO readOne(Long mid);

    // 수정
    void modify(ShippingDTO shippingDTO);

    // 삭제
    void remove(Long dno);

    void modifyDdefault(String mid);

}
