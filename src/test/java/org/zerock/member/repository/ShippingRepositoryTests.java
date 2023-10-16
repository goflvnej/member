package org.zerock.member.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.member.domain.Board;
import org.zerock.member.domain.Shipping;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class ShippingRepositoryTests {

    @Autowired
    private ShippingRepository shippingRepository;

    @Test
    public void testSelect() {
        String mid = "member100";

        List<Shipping> result = shippingRepository.findAllByMidOrderByDdefaultDesc(mid);

        result.forEach(shipping -> log.info(shipping));
    }

    @Test
    public void testReadOne() {
        Long dno = 1L;

        Optional<Shipping> shipping = shippingRepository.findByDno(dno);

        log.info(shipping);
    }
}
