package com.udacity.vehicles.event;

import com.udacity.vehicles.domain.car.Car;
import org.springframework.context.ApplicationEvent;

public class VehicleEvent extends ApplicationEvent {

    private CarEventsEnum eventType;

    private Car car;

    public VehicleEvent(Object source, CarEventsEnum eventType, Car car) {
        super(source);
        this.eventType = eventType;
        this.car = car;
    }

    public CarEventsEnum getEventType() {
        return eventType;
    }

    public Car getCar() {
        return car;
    }
}
