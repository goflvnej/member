package org.zerock.member.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.member.dto.ShippingDTO;

import java.util.List;

@SpringBootTest
@Log4j2
public class ShippingServiceTests {

    @Autowired
    private ShippingService shippingService;

    @Test
    public void testReadAll() {

        List<ShippingDTO> shippingDTOList = shippingService.readAll("member100");

        log.info(shippingDTOList);
    }

    @Test
    public void testReadOne() {

        ShippingDTO shippingDTO = shippingService.readOne(1L);

        log.info(shippingDTO);
    }
}
