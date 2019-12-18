package com.udacity.pricing.service;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceEntity;
import com.udacity.pricing.domain.price.PriceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class PricingService {

    private PriceRepository repository;

    @Autowired
    private final ModelMapper modelMapper;

    public Price findPriceById(Long id){
        Optional<PriceEntity> priceEntity = repository.findById(id);
        PriceEntity entity =priceEntity.orElseThrow(PriceNotFoundException::new);
        Price price = new Price();
        modelMapper.map(Objects.requireNonNull(entity), price);
        return price;

    }

    public Price findPriceByVehicleId(Long vehicleId){
        Optional<PriceEntity> priceEntity = repository.findPriceEntityByVehicleId(vehicleId);
        PriceEntity entity =priceEntity.orElseThrow(PriceNotFoundException::new);
        Price price = new Price();
        modelMapper.map(Objects.requireNonNull(entity), price);
        return price;
    }

    public Price updatePriceByVehicleId(Price price){
        Optional<PriceEntity> priceEntity = repository.findPriceEntityByVehicleId(price.getVehicleId());
        PriceEntity entity =priceEntity.orElseThrow(PriceNotFoundException::new);
        entity.setPrice(price.getPrice());
        entity.setCurrency(price.getCurrency());
        repository.save(entity);
        return price;
    }

    public void createPrice(Price price){
        PriceEntity priceEntity = PriceEntity.builder().currency(price.getCurrency())
                                   .vehicleId(price.getVehicleId())
                                    .price(price.getPrice()).build();
        repository.save(priceEntity);
    }

    public void deletePriceByVehicleId(Price price){
        Optional<PriceEntity> priceEntity = repository.findPriceEntityByVehicleId(price.getVehicleId());
        PriceEntity entity =priceEntity.orElseThrow(PriceNotFoundException::new);
        repository.deleteByVehicleId(entity.getVehicleId());
    }

    public void deletePriceById(Long id){
        Optional<PriceEntity> optionalPriceEntity = repository.findById(id);
        PriceEntity entity = optionalPriceEntity.orElseThrow(PriceNotFoundException::new);
        repository.delete(entity);
    }
}
