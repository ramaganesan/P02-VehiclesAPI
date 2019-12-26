package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;
import java.util.List;
import java.util.Optional;

import com.udacity.vehicles.event.CarEventsEnum;
import com.udacity.vehicles.event.VehicleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService implements ApplicationEventPublisherAware {

    private final CarRepository repository;

    private final PriceClient priceClient;

    private final MapsClient mapsClient;

    private ApplicationEventPublisher publisher;

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    public CarService(CarRepository repository, PriceClient priceClient, MapsClient mapsClient) {
        /**
         * TODO: Add the Maps and Pricing Web Clients you create
         *   in `VehiclesApiApplication` as arguments and set them here.
         */
        this.repository = repository;
        this.priceClient = priceClient;
        this.mapsClient = mapsClient;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {
        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */
        Optional<Car> optionalCar = repository.findById(id);
        Car car = optionalCar.orElseThrow(CarNotFoundException::new);

        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
         try{
             car.setPrice(priceClient.getPrice(car.getId()));
         }catch (Exception e){
             logger.error("Error getting Price details from Pricing Service " + e.getLocalizedMessage());
         }

        /**
         * TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */
         try{
             car.setLocation(mapsClient.getAddress(car.getLocation()));
         }catch (Exception e){
             logger.error("Error getting Location Information from Location Service " + e.getLocalizedMessage());
         }

        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            Car finalCar = car;
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(finalCar.getDetails());
                        carToBeUpdated.setLocation(finalCar.getLocation());
                        carToBeUpdated = repository.save(carToBeUpdated);
                        carToBeUpdated.setPrice(finalCar.getPrice());
                        publisher.publishEvent(new VehicleEvent(this, CarEventsEnum.CREATED,carToBeUpdated));
                        return carToBeUpdated;
                    })
                    .orElseThrow(CarNotFoundException::new);
        }
        car = repository.save(car);
        publisher.publishEvent(new VehicleEvent(this, CarEventsEnum.CREATED,car));
        return car;
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
         Optional<Car> optionalCar = repository.findById(id);
         Car car = optionalCar.orElseThrow(CarNotFoundException::new);

        /**
         * TODO: Delete the car from the repository.
         */
         repository.delete(car);
        publisher.publishEvent(new VehicleEvent(this, CarEventsEnum.DELETED,car));

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;

    }
}
