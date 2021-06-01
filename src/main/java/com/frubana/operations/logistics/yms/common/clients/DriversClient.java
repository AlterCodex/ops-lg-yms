package com.frubana.operations.logistics.yms.common.clients;

import com.frubana.operations.logistics.yms.yard.domain.Yard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * External some object client definition.
 */
@Component(value = "DriversServiceClient")
public class DriversClient {

    private final RestTemplate restTemplate;

    @Autowired
    public DriversClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<DriversDTO> getDriversWaitingByWarehouse(String warehouse) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:3000/drivers/")
                .queryParam("warehouse", warehouse);
        ParameterizedTypeReference<Map<String, List<DriversDTO>>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<Map<String, List<DriversDTO>>> response = this.restTemplate.exchange(builder.build().toUri(),
                HttpMethod.GET, null, typeRef);

        return response.getBody().get("drivers");
    }
}
