package org.zerock.member.service;

import org.zerock.member.domain.Shipping;
import org.zerock.member.dto.BoardDTO;
import org.zerock.member.dto.ShippingDTO;

import java.util.ArrayList;
import java.util.List;

public interface ShippingService {

    // 등록
    Long register(ShippingDTO ShippingDTO);

    // 조회 (아이디 별로 전체)
    List<ShippingDTO> readAll(String mid);

    // 조회 (해당 주소 1개)
    ShippingDTO readOne(Long mid);

    // 수정
    void modify(ShippingDTO shippingDTO);

}
