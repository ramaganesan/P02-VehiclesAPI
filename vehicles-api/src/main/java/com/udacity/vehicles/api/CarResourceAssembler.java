package com.udacity.vehicles.api;

import com.udacity.vehicles.domain.car.Car;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;



import static org.springframework.hateoas.server.core.WebHandler.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Maps the CarController to the Car class using HATEOAS
 */
@Component
public class CarResourceAssembler implements RepresentationModelAssembler<Car, EntityModel<Car>> {

    /*//<editor-fold desc="Description">
    @Override
    public Resource<Car> toResource(Car car) {
        return new Resource<>(car,
                linkTo(methodOn(CarController.class).get(car.getId())).withSelfRel(),
                linkTo(methodOn(CarController.class).list()).withRel("cars"));

    }
    //</editor-fold>*/

    @Override
    public EntityModel<Car> toModel(Car entity) {
        return new EntityModel<Car>(entity,
                WebMvcLinkBuilder.linkTo(methodOn(CarController.class).get(entity.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(CarController.class).list()).withRel("cars")
         );
    }
}
