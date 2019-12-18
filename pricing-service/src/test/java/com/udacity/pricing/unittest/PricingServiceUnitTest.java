package com.udacity.pricing.unittest;

import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.domain.price.PriceEntity;
import com.udacity.pricing.domain.price.PriceRepository;
import com.udacity.pricing.service.PricingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.mockito.ArgumentMatchers;
import org.mockito.AdditionalAnswers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PricingServiceUnitTest {


    private PriceRepository priceRepository = Mockito.mock(PriceRepository.class);

    private ModelMapper modelMapper = new ModelMapper();


    private PricingService pricingService;

    @BeforeAll
    public void init() {
        System.out.println("---Inside initAll---");
        pricingService = new PricingService(priceRepository,modelMapper);
    }

    @Test
    public void findPriceByIdTest(){
        PriceEntity priceEntity = PriceEntity.builder().price(new BigDecimal(20000)).id(1l).vehicleId(200L).currency("USD").build();
        Mockito.when(priceRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(priceEntity));
        Price price = pricingService.findPriceById(1l);
        assertThat(price.getVehicleId()).isSameAs(priceEntity.getVehicleId());
    }

    @Test
    public void findPriceByVehicleIdTest(){
        PriceEntity priceEntity = PriceEntity.builder().price(new BigDecimal(20000)).id(1l).vehicleId(200L).currency("USD").build();
        Mockito.when(priceRepository.findPriceEntityByVehicleId(1L)).thenReturn(java.util.Optional.ofNullable(priceEntity));
        Price price = pricingService.findPriceByVehicleId(1l);
        assertThat(price.getVehicleId()).isSameAs(priceEntity.getVehicleId());
    }

    @Test
    public void updatePriceByVehicleIdTest(){
        PriceEntity priceEntity = PriceEntity.builder().price(new BigDecimal(20000)).id(1l).vehicleId(200L).currency("USD").build();
        Mockito.when(priceRepository.findPriceEntityByVehicleId(1L)).thenReturn(java.util.Optional.ofNullable(priceEntity));
        Price price = new Price("USD",new BigDecimal(30000),  1L);
        Mockito.when(priceRepository.save(ArgumentMatchers.any(PriceEntity.class))).then(AdditionalAnswers.returnsFirstArg());
        Price price1 = pricingService.updatePriceByVehicleId(price);
        assertThat(price).isSameAs(price1);
    }

    @Test
    public void createPriceTest(){

        Price price = new Price("USD",new BigDecimal(30000),  200L);
        pricingService.createPrice(price);
        ArgumentCaptor<PriceEntity> priceEntityArgumentCaptor = ArgumentCaptor.forClass(PriceEntity.class);
        Mockito.verify(priceRepository,Mockito.times(1)).save(priceEntityArgumentCaptor.capture());
        PriceEntity priceEntity = priceEntityArgumentCaptor.getValue();
        assertThat(priceEntity.getVehicleId()).isEqualTo(price.getVehicleId());
    }

    @Test
    public void deletePriceByVehicleIdTest(){
        Price price = new Price("USD",new BigDecimal(30000),  200L);
        PriceEntity priceEntity = PriceEntity.builder().price(new BigDecimal(30000)).id(1l).vehicleId(200L).currency("USD").build();
        Mockito.when(priceRepository.findPriceEntityByVehicleId(200L)).thenReturn(java.util.Optional.ofNullable(priceEntity));
        pricingService.deletePriceByVehicleId(price);
        ArgumentCaptor<Long> priceEntityArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(priceRepository,Mockito.times(1)).deleteByVehicleId(priceEntityArgumentCaptor.capture());
        Long priceEntityId = priceEntityArgumentCaptor.getValue();
        assertThat(priceEntityId).isEqualTo(price.getVehicleId());
    }


}
