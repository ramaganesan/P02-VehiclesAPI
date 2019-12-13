package com.udacity.pricing.components;

import com.udacity.pricing.domain.price.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PriceRepositoryInitRunner implements CommandLineRunner {

    private final PriceRepository priceRepository;

    public PriceRepositoryInitRunner(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
