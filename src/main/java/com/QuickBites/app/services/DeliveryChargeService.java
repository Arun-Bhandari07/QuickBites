package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.QuickBites.app.Exception.BadRequestException;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class DeliveryChargeService {

	@Value("${resturant.location.latitude}")
	private double lat;
	
	@Value("${resturant.location.longitude}")
	private double lon;
	
	@Value("${openrouteservice.api.base-url}")
	private String orsBaseUrl;
	
	@Value("${openrouteservice.api.key}")
	private String key;
	
	private static final BigDecimal RATE_PER_KM = BigDecimal.valueOf(10.0);
	private static final BigDecimal BASE_DELIVERY_CHARGE = BigDecimal.valueOf(20.0);
	private static final double MAX_DELIVERY_DISTANCE = 20.0;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	public double calculateDistance(double userLat, double userLon) {
		
		String uri = orsBaseUrl+"/v2/directions/driving-car";
		
		HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("Authorization", key);
			
		Map<String,Object> payload = Map.of("coordinates",
				List.of(List.of(lon,lat)
				,List.of(userLon, userLat)));
		
		HttpEntity<Map<String,Object>> entity = new HttpEntity<>(payload,headers);
		
		ResponseEntity<JsonNode> response = restTemplate.exchange(uri,HttpMethod.POST, entity,JsonNode.class);
				
		if(response.getStatusCode() == HttpStatus.OK) {
		double distance = response.getBody()
							.path("routes").get(0)
							.path("summary")
							.path("distance")
							.asDouble();
			
			System.out.println(distance);
			return distance/1000;
		}
		else {
			throw new RuntimeException("Error fetching distance from ORS");
		}
		
	}
	
	
	public BigDecimal calculateDeliveryCharge(double userLat , double userLon) {
		double distanceKm = calculateDistance(userLat, userLon);
		if(distanceKm>MAX_DELIVERY_DISTANCE) {
			throw new BadRequestException("Cannot deliver at the given location");
		}
		BigDecimal distanceInBigDecimal = BigDecimal.valueOf(distanceKm);
		
		BigDecimal deliveryCharge = BASE_DELIVERY_CHARGE.add(distanceInBigDecimal.multiply(RATE_PER_KM));
		return deliveryCharge.setScale(0, RoundingMode.UP);
		 
		
	}
	
	
	
}
