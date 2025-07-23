package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
public class DeliveryRouteService {

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
	
	public record DeliveryStats(Double deliveryDistance , Long deliveryTime ) {};
	
	public record DeliveryInfo(BigDecimal deliveryCharge , Long deliveryTime ) {};
	
	public record DeliveryRouteResponse(
		    double distanceKm,
		    long durationSeconds,
		    List<List<Double>> pathCoordinates
		) {}
	
	public DeliveryStats calculateDistance(double userLat, double userLon) {
		
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
		double deliveryDistance = response.getBody()
							.path("routes").get(0)
							.path("summary")
							.path("distance")
							.asDouble();
			
		Long deliveryTime = response.getBody()
							.path("routes").get(0)
							.path("summary")
							.path("duration")
							.asLong();
				
			DeliveryStats stats= new DeliveryStats(deliveryDistance/1000,deliveryTime);
			return stats;
		}
		else {
			throw new RuntimeException("Error fetching distance from ORS");
		}
		
	}
	
	
	public DeliveryRouteResponse getDeliveryRoute(double userLat, double userLon) {
	    String uri = orsBaseUrl + "/v2/directions/driving-car/geojson";

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Authorization", key);

	    Map<String, Object> payload = Map.of("coordinates",
	            List.of(List.of(lon, lat), List.of(userLon, userLat)));

	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

	    ResponseEntity<JsonNode> response = restTemplate.exchange(uri, HttpMethod.POST, entity, JsonNode.class);

	    if (response.getStatusCode() == HttpStatus.OK) {
	        JsonNode root = response.getBody();
	        JsonNode feature = root.path("features").get(0);
	        JsonNode summary = feature.path("properties").path("summary");

	        double distance = summary.path("distance").asDouble() / 1000.0;
	        long duration = summary.path("duration").asLong();

	        List<List<Double>> path = new ArrayList<>();
	        for (JsonNode coord : feature.path("geometry").path("coordinates")) {
	            // GeoJSON format is [lon, lat]
	            path.add(List.of(coord.get(1).asDouble(), coord.get(0).asDouble())); // [lat, lon]
	        }

	        return new DeliveryRouteResponse(distance, duration, path);
	    }

	    throw new RuntimeException("Failed to fetch route from ORS");
	}
	
	
	
	
	
	public DeliveryInfo calculateDeliveryChargeAndTime(double userLat , double userLon) {
		
		//calculate Delivery Charge with respect to distance
		DeliveryStats stats = calculateDistance(userLat, userLon);
		double distanceKm =stats.deliveryDistance ;
		if(distanceKm>MAX_DELIVERY_DISTANCE) {
			throw new BadRequestException("Cannot deliver at the given location");
		}
		BigDecimal distanceInBigDecimal = BigDecimal.valueOf(distanceKm);
		
		BigDecimal deliveryCharge = BASE_DELIVERY_CHARGE.add(distanceInBigDecimal.multiply(RATE_PER_KM));
		
		deliveryCharge = deliveryCharge.setScale(0, RoundingMode.UP);
		
		Long deliveryTime = stats.deliveryTime;
		
		DeliveryInfo info = new DeliveryInfo(deliveryCharge, calculateDeliveryDuration(deliveryTime));
		return info;
		 
	}
	
	public Long calculateDeliveryDuration(Long durationInSeconds) {
		Long durationInMinutes = durationInSeconds/60;
	
		return durationInMinutes.longValue();
	}
	
	
	
}
