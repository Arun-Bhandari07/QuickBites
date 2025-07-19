package com.QuickBites.app.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.OrderResponseDTO;
import com.QuickBites.app.DTO.StatusUpdateDTO;
import com.QuickBites.app.enums.KitchenStatus;
import com.QuickBites.app.services.StaffService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/api/v1/staff/")
@RestController
@Tag(name="Kitchen Staff Operations" , description= "Change the status of food Preparation")
public class StaffController {
	
	private final StaffService staffService;
	
	public StaffController(StaffService staffService) {
		this.staffService = staffService;
	}
	
	@GetMapping("/orders")
	public List<OrderResponseDTO> getAllOrders() {
		return staffService.getOrderForKitchenDashboard();
	}
	
	@GetMapping("/kitchen-statuses")
	public List<String> getKitchenStatus(){
		return Arrays.stream(KitchenStatus.values())
				.map(status -> status.name())
				.collect(Collectors.toList());
	}
	
	@PatchMapping("/orders/{id}/status")
	public ResponseEntity<?> updateKitchenStatus(@Valid @PathVariable(name="id") long id
												, @RequestBody StatusUpdateDTO dto){
		
		staffService.updateKitchenStatus(id,dto);
		return ResponseEntity.ok().body("Updated Kitchen Status");
	}
	
}


