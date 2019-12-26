package com.udacity.pricing.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.pricing.domain.price.Price;
import com.udacity.pricing.service.PricingService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest()
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PricingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PricingService pricingService;

    private static final String basePath = "/services/price/";

    @Test
    @Order(2)
    public void getPriceById() throws Exception {
       Long id = 1L;
       Price price = new Price("USD",new BigDecimal(100000),100L);
       price.setId(1L);
       Mockito.when(pricingService.findPriceById(id)).thenReturn(price);
       mockMvc.perform(get(basePath+id))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(price)));
   }

   @Test
   @Order(3)
    public void getByVehicleId() throws Exception{
        Long vehicleId = 100L;
       Price price = new Price("USD",new BigDecimal(100000),100L);
       Mockito.when(pricingService.findPriceByVehicleId(vehicleId)).thenReturn(price);
       mockMvc.perform(get(basePath+"/vehicle/")
                       .param("vehicleId", vehicleId.toString()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json(objectMapper.writeValueAsString(price)));
   }

   @Test
   @Order(1)
   public void createPrice() throws Exception{
       Price price = new Price("USD",new BigDecimal(100000),100L);
       ArgumentCaptor<Price> priceArgumentCaptor = ArgumentCaptor.forClass(Price.class);
       mockMvc.perform(post(basePath)
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(price)))
                       .andExpect(status().isCreated());
       verify(pricingService,times(1)).createPrice(priceArgumentCaptor.capture());

   }

   @Test
   @Order(4)
    public void updatePrice() throws Exception{
       Price price = new Price("USD",new BigDecimal(100000),100L);
       price.setId(1l);
       ArgumentCaptor<Price> priceArgumentCaptor = ArgumentCaptor.forClass(Price.class);
       mockMvc.perform(put(basePath+"{id}",1l)
               .contentType("application/json")
               .content(objectMapper.writeValueAsString(price)))
               .andExpect(status().isNoContent());
       verify(pricingService,times(1)).updatePriceByVehicleId(priceArgumentCaptor.capture());
   }

   @Test
   @Order(5)
   public void deletePrice() throws Exception{
        Long id = 1L;
       ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);
       mockMvc.perform(delete(basePath+"{id}",1l))
               .andExpect(status().isNoContent());
       verify(pricingService,times(1)).deletePriceById(longArgumentCaptor.capture());

    }
}
