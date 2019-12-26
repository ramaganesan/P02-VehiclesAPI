package com.udacity.vehicles.components;

import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class CarRepositoryInitRunner implements CommandLineRunner {

    private final CarRepository carRepository;

    private final Logger logger = LoggerFactory.getLogger(CarRepositoryInitRunner.class);

    public CarRepositoryInitRunner(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Init Car");
        carRepository.save(getCar());
    }

    private static Details getDetails(){
        Details details = new Details();
        details.setBody("Four-Door-Sedan");
        details.setModel("Focus");
        Manufacturer manufacturer = new Manufacturer(102, "Ford");
        details.setManufacturer(manufacturer);
        details.setNumberOfDoors(4);
        details.setFuelType("Gasoline");
        details.setEngine("V4");
        details.setMileage(200);
        details.setModelYear(2019);
        details.setProductionYear(2016);
        return details;
    }

    private static Car getCar(){
        Car car = new Car();
        car.setCondition(Condition.NEW);
        car.setDetails(getDetails());
        Location location = new Location(new Double(10), new Double(10));
        car.setLocation(location);
        return car;
    }
}
