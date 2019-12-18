package com.udacity.pricing.unittest;

import com.udacity.pricing.domain.price.PriceEntity;
import com.udacity.pricing.domain.price.PriceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@Sql("/priceTestLoad.sql")
public class PricingRepositoryTest {

    @Autowired
    private PriceRepository priceRepository;


    @Test

    void findPriceEntityByVehicleIdTest(){
        Optional<PriceEntity> priceEntity = priceRepository.findPriceEntityByVehicleId(100L);
        assertThat(priceEntity.get().getVehicleId()).isEqualTo(100L);
    }

    @Test

    void deleteByVehicleIdTest(){
        priceRepository.deleteByVehicleId(100L);
        Optional<PriceEntity> optionalPriceEntity = priceRepository.findPriceEntityByVehicleId(100L);
        assertThat(optionalPriceEntity.isPresent()).isEqualTo(false);
    }
}
