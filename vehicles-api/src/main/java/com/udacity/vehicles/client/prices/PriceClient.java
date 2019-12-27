package com.udacity.vehicles.client.prices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;
import reactor.retry.RetryContext;

import java.time.Duration;
import java.util.function.Predicate;

/**
 * Implements a class to interface with the Pricing Client for price data.
 */
@Component
public class PriceClient  {

    private static final Logger log = LoggerFactory.getLogger(PriceClient.class);

    private final WebClient client;

    public PriceClient(WebClient pricing) {
        this.client = pricing;
    }

    public Retry<?> retryPrice = Retry.onlyIf(getRetryContextPredicate())
                                   .retryMax(3)
                                    .fixedBackoff(Duration.ofSeconds(3))
                                    .doOnRetry(objectRetryContext -> {
                                        log.error(objectRetryContext.exception().getMessage());
                                    });


    // In a real-world application we'll want to add some resilience
    // to this method with retries/CB/failover capabilities
    // We may also want to cache the results so we don't need to
    // do a request every time
    /**
     * Gets a vehicle price from the pricing client, given vehicle ID.
     * @param vehicleId ID number of the vehicle for which to get the price
     * @return Currency and price of the requested vehicle,
     *   error message that the vehicle ID is invalid, or note that the
     *   service is down.
     */
    @Cacheable(cacheNames = "pricing-service-cache", key = "#vehicleId", unless = "#result == '(consult price)'")
    public String getPrice(Long vehicleId) {
        log.info("Cache Missing Calling Pricing Service");
        try {
            Price price = client
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("services/price/vehicle")
                            .queryParam("vehicleId", vehicleId)
                            .build()
                    ).retrieve()
                    .bodyToMono(Price.class)
                    .retryWhen(retryPrice)
                    .block();

            return String.format("%s %s", price.getCurrency(), price.getPrice());

        }catch (WebClientResponseException e){
            log.error("Error retrieving price for vehicle {}", vehicleId, e);
        }
        catch (Exception e) {
            log.error("Unexpected error retrieving price for vehicle {}", vehicleId, e);
        }
        return "(consult price)";
    }

    private Predicate<RetryContext<Object>> getRetryContextPredicate() {
        return retryContext -> {
            if(retryContext.exception() instanceof WebClientResponseException &&
                    ((WebClientResponseException) retryContext.exception()).getStatusCode().is5xxServerError()){
                return true;
            }
            return false;
        };
    }

    public Price createOrUpdatePrice(Price price){
        Price returnPrice = client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("services/price/vehicle")
                        .build())
                .body(Mono.just(price),Price.class)
                .retrieve()
                .bodyToMono(Price.class)
                .retryWhen(retryPrice)
                .block();
        log.info("Successfully updated Pricing service " + returnPrice.getVehicleId());
        return returnPrice;
    }

    public void deletePrice(Long id){
        client.delete()
                .uri(uriBuilder -> uriBuilder.path("services/price/vehicle/")
                        .queryParam("vehicleId", id)
                        .build())
                .retrieve()
                .toBodilessEntity()
                .retryWhen(retryPrice)
                .block();
        log.info("Successfully deleted Price");
    }

}
