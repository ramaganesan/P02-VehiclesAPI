package com.udacity.pricing.integrationtest;

import com.udacity.pricing.domain.price.Price;
import org.aspectj.util.LangUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, args={"eureka.client.enabled:false"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PricingController {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final static String hostWithProtocol = "http://localhost:";

    private final static String basePath = "/services/price/";

    @Test
    @org.junit.jupiter.api.Order(1)
    public void getByVehicleId(){
        Long vehicleId = 1l;
        Price price = testRestTemplate.getForObject(hostWithProtocol+port+basePath+"/vehicle?vehicleId="+vehicleId.toString(),Price.class);
        assertThat(price).isNotNull();
        assertThat(price.getVehicleId()).isEqualTo(vehicleId);

    }

    @Test
    @Order(2)
    public void getPriceById(){
        Long id = 1L;
        Long vehicleId = 1l;
        Price price = testRestTemplate.getForObject(hostWithProtocol+port+basePath+id.toString(),Price.class);
        assertThat(price).isNotNull();
        assertThat(price.getVehicleId()).isEqualTo(vehicleId);
    }

    @Test
    @Order(3)
    public void createPrice(){
        Price price = new Price("USD",new BigDecimal(400000),400L);
        testRestTemplate.postForLocation(hostWithProtocol+port+basePath,price);
        Price price1 = testRestTemplate.getForObject(hostWithProtocol+port+basePath+"/vehicle?vehicleId="+price.getVehicleId().toString(),Price.class);
        assertThat(price1).isNotNull();
        assertThat(price1.getVehicleId()).isEqualTo(price.getVehicleId());
    }

    @Test
    @Order(4)
    public void updatePrice(){
        Long vehicleId = 1l;
        Price price = testRestTemplate.getForObject(hostWithProtocol+port+basePath+"/vehicle?vehicleId="+vehicleId.toString(),Price.class);
        BigDecimal newPrice = price.getPrice().add(new BigDecimal(200000));
        price.setPrice(newPrice);
        testRestTemplate.put(hostWithProtocol+port+basePath+price.getId().toString(),price);
        Price price1 = testRestTemplate.getForObject(hostWithProtocol+port+basePath+"/vehicle?vehicleId="+vehicleId.toString(),Price.class);
        assertThat(price1).isNotNull();
        assertThat(price1.getVehicleId()).isEqualTo(price.getVehicleId());
        assertThat(price1.getPrice()).isEqualTo(newPrice);
    }


}
