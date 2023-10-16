package org.zerock.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.member.domain.Shipping;

import java.util.List;
import java.util.Optional;

public interface ShippingRepository extends JpaRepository<Shipping, Long> {

    // mid(유저 아이디)로 Shipping 테이블의 모든 데이터를 가져온다. -> 기본배송지 내림차순
    List<Shipping> findAllByMidOrderByDdefaultDesc(String mid);

    // dno(기본키)로 데이터를 가져온다.
    Optional<Shipping> findByDno(Long dno);

    // 기본 배송지 설정 시 나머지 데이터를 기본 배송지에서 해제
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Shipping s SET s.ddefault = NULL WHERE s.mid = :mid")
    void updateDdeafult(@Param("mid") String mid);
}
