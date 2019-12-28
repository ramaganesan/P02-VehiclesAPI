package com.udacity.vehicles.event;

import com.udacity.vehicles.client.prices.Price;
import com.udacity.vehicles.client.prices.PriceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
@Component
/**
 * This class will process Vehicle Events Created
 * Based on the event type, it will create, update or delete price for a Vehicle
 * in Pricing API
 */
public class VehicleEventProcessor implements ApplicationListener<VehicleEvent> {

    private static final Logger log = LoggerFactory.getLogger(VehicleEventProcessor.class);

    private final PriceClient priceClient;

    private final CacheManager cacheManager;

    public VehicleEventProcessor(PriceClient priceClient, CacheManager cacheManager)
    {
        this.priceClient = priceClient;
        this.cacheManager = cacheManager;
    }

    /**
     * Update price for the Vehicle in Pricing Service
     * @param vehicleEvent
     */
    @Override
    public void onApplicationEvent(VehicleEvent vehicleEvent) {
        VehicleEvent event = (VehicleEvent) vehicleEvent;
        if(event.getEventType().equals(CarEventsEnum.CREATED) || event.getEventType().equals(CarEventsEnum.UPDATED)){
            log.info("Cars update or created event published. Updating Price with this data");
            Price price = new Price();
            price.setCurrency("USD");
            if(StringUtils.isEmpty(vehicleEvent.getCar().getPrice())){
                price.setPrice(new BigDecimal("-1"));
            }else{
                price.setPrice(new BigDecimal(vehicleEvent.getCar().getPrice()));
            }

            price.setVehicleId(vehicleEvent.getCar().getId());
            Price returnPrice = priceClient.createOrUpdatePrice(price);

            log.info("Successfully updated Pricing service " + returnPrice.getVehicleId());

        }else{
            log.info("Cars delete event published. Deleting Price");
            priceClient.deletePrice(vehicleEvent.getCar().getId());
            log.info("Successfully deleted Price");
        }
        pricingCacheClear(vehicleEvent.getCar().getId());
    }

   // @CacheEvict(cacheNames ="pricing-service-cache")
    /**
    * This method evicts Pricing Service Cache
    * @param vehicleId
     **/
    public void pricingCacheClear(Long vehicleId){
        cacheManager.getCache("pricing-service-cache").evictIfPresent(vehicleId);
        log.info("Cleared  Price cache for vehicle id: " + vehicleId);
    }
}
