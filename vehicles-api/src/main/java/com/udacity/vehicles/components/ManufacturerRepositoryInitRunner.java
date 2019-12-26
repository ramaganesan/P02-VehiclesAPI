package com.udacity.vehicles.components;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.domain.manufacturer.ManufacturerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
/**
 * Initializes the car manufacturers available to the Vehicle API.
 * @param repository where the manufacturer information persists.
 * @return the car manufacturers to add to the related repository
 */
@Order(1)
public class ManufacturerRepositoryInitRunner implements CommandLineRunner {

    private final ManufacturerRepository manufacturerRepository;

    private final static Logger LOGGER = LoggerFactory.getLogger(ManufacturerRepositoryInitRunner.class);

    public ManufacturerRepositoryInitRunner(ManufacturerRepository manufacturerRepository){
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void run(String... args) throws Exception{
        LOGGER.info("Init Manufacturer");
        manufacturerRepository.save(new Manufacturer(100, "Audi"));
        manufacturerRepository.save(new Manufacturer(101, "Chevrolet"));
        manufacturerRepository.save(new Manufacturer(102, "Ford"));
        manufacturerRepository.save(new Manufacturer(103, "BMW"));
        manufacturerRepository.save(new Manufacturer(104, "Dodge"));
    }
}
