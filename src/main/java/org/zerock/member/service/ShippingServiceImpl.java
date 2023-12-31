package org.zerock.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.member.entity.Shipping;
import org.zerock.member.dto.ShippingDTO;
import org.zerock.member.repository.ShippingRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ShippingServiceImpl implements ShippingService {

    private final ModelMapper modelMapper;          // 엔티티(Board)와 DTO(BoardDTO) 간의 변환
    private final ShippingRepository shippingRepository;  // 엔티티 객체 연결

    // 등록
    @Override
    public Long register(ShippingDTO ShippingDTO) {
        Shipping shipping = modelMapper.map(ShippingDTO, Shipping.class);
        Long dno = shippingRepository.save(shipping).getSno();

        return dno;
    }

    // 조회 -> mid(사용자 아이디)로 배송 주소 DTO 전체 리스트 리턴
    @Override
    public List<ShippingDTO> readAll(String mid) {

        List<Shipping> shippingList = shippingRepository.listOfShipping(mid);

        List<ShippingDTO> shippingDTOList = shippingList.stream()
                    .map(data -> modelMapper.map(data, ShippingDTO.class))
                    .collect(Collectors.toList());

        return shippingDTOList;
    }

    // 조회 -> dno(PK)로 배송 주소 모달창으로 1개 리턴
    @Override
    public ShippingDTO readOne(Long sno) {

        Optional<Shipping> result = shippingRepository.findBySno(sno);
        Shipping shipping = result.orElseThrow();
        ShippingDTO shippingDTO = modelMapper.map(shipping, ShippingDTO.class);

        return shippingDTO;
    }

    @Override
    public void modify(ShippingDTO shippingDTO) {

        Optional<Shipping> result = shippingRepository.findBySno(shippingDTO.getSno());
        Shipping shipping = result.orElseThrow();
        shipping.change(shippingDTO.getSname(), shippingDTO.getSperson(), shippingDTO.getZonecode(), shippingDTO.getAddress(), shippingDTO.getSaddress(), shippingDTO.getSdefault());
        shippingRepository.save(shipping);

    }

    @Override
    public void remove(Long dno) {

        shippingRepository.deleteById(dno);
    }

    @Override
    public void modifySdefault(String mid) {

        shippingRepository.updateSdeafult(mid);
    }


}
