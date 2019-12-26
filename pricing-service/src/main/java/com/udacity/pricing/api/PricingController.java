package com.udacity.pricing.api;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PriceException;
import com.udacity.pricing.service.PriceNotFoundException;
import com.udacity.pricing.service.PricingMockService;
import com.udacity.pricing.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implements a REST-based controller for the pricing service.
 */
@RestController
@RequestMapping("/services/price")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    /**
     * Gets the price for a requested vehicle.
     * @param vehicleId ID number of the vehicle for which the price is requested
     * @return price of the vehicle, or error that it was not found.
     */
    @GetMapping("/vehicle")
    public Price getByVehicleId(@RequestParam Long vehicleId) {
        try {
            return pricingService.findPriceByVehicleId(vehicleId);
            //throw new RuntimeException("No price");
        } catch (PriceNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Price Not Found for vehicleId: " + vehicleId, ex);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in finding Price for vehicleId: " + vehicleId, ex);
        }

    }

    @PostMapping("/vehicle")
    public Price savePriceByVehicleId(@RequestBody Price price){
        try{
            return pricingService.savePriceByVehicleId(price);
        }catch (Exception e){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in saving Price for vehicleId: " + price.getVehicleId(), e);
        }
    }

    @DeleteMapping("/vehicle")
    public void deletePriceByVehicleId(@RequestParam Long vehicleId){
        try {
            pricingService.deletePriceById(vehicleId);
        }catch (PriceNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Unable to delete Price for vehicleId: " + vehicleId, ex);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in deleting Price for vehicleId: " + vehicleId, ex);
        }
    }

    @GetMapping("/{id}")
    public Price getPriceById(@PathVariable Long id) {
        try {
            return pricingService.findPriceById(id);
        } catch (PriceNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Price Not Found for ID: " + id, ex);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in finding Price for Id: " + id, ex);
        }

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPrice(@RequestBody Price price){
        try {
            pricingService.createPrice(price);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in Creating Price for vehicleId: " + price.getVehicleId(), ex);
        }
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePrice(@RequestBody Price price, @PathVariable Long id){
        try{
             pricingService.updatePriceByVehicleId(price);
            //throw new PriceNotFoundException("No price");
        }catch (PriceNotFoundException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Unable to update Price as Price is Not Found for id: " + id, ex);
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in updating price for vehicleId: " + price.getVehicleId(), ex);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePrice(@PathVariable Long id){
        try{
            pricingService.deletePriceById(id);
        }catch (PriceNotFoundException ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Unable to delete price for id: " + id, ex);
        }catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error in deleting Price: " + id, ex);
        }
    }
}
