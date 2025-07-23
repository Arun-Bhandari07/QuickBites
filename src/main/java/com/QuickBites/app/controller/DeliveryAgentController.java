package com.QuickBites.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AgentDashboardDTO;
import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.DeliveryHistoryDTO;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.services.DeliveryAgentService;
import com.QuickBites.app.services.DeliveryRouteService;
import com.QuickBites.app.services.DeliveryRouteService.DeliveryRouteResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/agent")
@Tag(name="Delivery-Agent ")
public class DeliveryAgentController {
	
	@Autowired
	DeliveryAgentService deliveryAgentService;
	
	@Autowired
	DeliveryAgentRepository deliveryAgentRepo;
	
	@Autowired
	DeliveryRouteService deliveryRouteService;
	 
	
	@GetMapping("/delivery-route")
	@Operation(summary="Get DeliveryRoute")
	public ResponseEntity<DeliveryRouteResponse> getRouteToUser(
	        @RequestParam(name="lat") double lat,
	        @RequestParam(name="lon") double lon) {
	    return ResponseEntity.ok(deliveryRouteService.getDeliveryRoute(lat, lon));
	}
	
	@Operation(summary = "Get Dashboard Summary")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<AgentDashboardDTO>> getDashboardSummary(Authentication authentication) {
        String username = authentication.getName();
        AgentDashboardDTO dashboardData = deliveryAgentService.getAgentDashboardData(username);
        
        ApiResponse<AgentDashboardDTO> response = new ApiResponse<>("success", "Dashboard summary retrieved successfully.", dashboardData);
        
        return ResponseEntity.ok(response);
    }
	
    @Operation(summary = "Get Delivery History")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<DeliveryHistoryDTO>>> getDeliveryHistory(Authentication authentication) {
        String username = authentication.getName();
        List<DeliveryHistoryDTO> history = deliveryAgentService.getAgentDeliveryHistory(username);
        
        ApiResponse<List<DeliveryHistoryDTO>> response = new ApiResponse<>("success", "Delivery history retrieved successfully.", history);
        
        return ResponseEntity.ok(response);
    }
	
	
	
}
