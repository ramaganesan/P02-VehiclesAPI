package com.udacity.pricing.components;

import com.udacity.pricing.domain.price.PriceEntity;
import com.udacity.pricing.domain.price.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class PriceRepositoryInitRunner implements CommandLineRunner {

    private final PriceRepository priceRepository;

    public PriceRepositoryInitRunner(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Init Price Repository");
        priceRepository.save(PriceEntity.builder().currency("USD").price(new BigDecimal(100000)).vehicleId(1L).build());
        priceRepository.save(PriceEntity.builder().currency("USD").price(new BigDecimal(200000)).vehicleId(2L).build());
        priceRepository.save(PriceEntity.builder().currency("USD").price(new BigDecimal(300000)).vehicleId(3L).build());
    }
}
