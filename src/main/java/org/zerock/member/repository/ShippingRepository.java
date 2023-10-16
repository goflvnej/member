package org.zerock.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.member.domain.Shipping;

import java.util.List;
import java.util.Optional;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    // mid(유저 아이디)로 Shipping 테이블의 모든 데이터를 가져온다 -> 기본배송지 내림차순
    List<Shipping> findAllByMidOrderByDdefaultDesc(String mid);
    
    Optional<Shipping> findByDno(Long dno);

}
