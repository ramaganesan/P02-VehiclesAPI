package com.udacity.pricing.domain.price;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PriceRepository extends CrudRepository<PriceEntity, Long> {

  public Optional<PriceEntity> findPriceEntityByVehicleId(Long vehicleId);

  public void  deleteByVehicleId(Long vehicleId);

}
