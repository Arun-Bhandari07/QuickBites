package com.QuickBites.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.CreateFoodCategoryDTO;
import com.QuickBites.app.DTO.CreateFoodItemDTO;
import com.QuickBites.app.DTO.CreateFoodVariantDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.DTO.FoodVariantResponseDTO;
import com.QuickBites.app.DTO.UpdateFoodCategoryDTO;
import com.QuickBites.app.DTO.UpdateFoodItemDTO;
import com.QuickBites.app.DTO.UpdateFoodVariantDTO;
import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.RegistrationException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.services.DeliveryAgentService;
import com.QuickBites.app.services.FoodCategoryService;
import com.QuickBites.app.services.FoodItemService;
import com.QuickBites.app.services.FoodVariantService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Validated
@RestController
@Tag(name="Admin Operations", description="Manage Food Menu and Users")
@RequestMapping("/api/v1/admin")
public class AdminController {
	private FoodCategoryService foodCategoryService;
	private FoodItemService foodItemService;
	private FoodVariantService foodVariantService;
	private PendingUserRepository pendingUserRepo;
	private DeliveryAgentService deliveryAgentService;
	
	public AdminController(FoodCategoryService foodCategoryService
			,FoodItemService foodItemService
			,FoodVariantService foodVariantService
			,PendingUserRepository pendingUserRepo
			,DeliveryAgentService deliveryAgentService) {
		this.foodCategoryService=foodCategoryService;
		this.foodItemService = foodItemService;
		this.foodVariantService = foodVariantService;
		this.pendingUserRepo=pendingUserRepo;
		this.deliveryAgentService=deliveryAgentService;
	}
	
	@GetMapping("/pending-agents")
	public List<PendingUser> getPendingAgents() {
		return pendingUserRepo.findByNotAdminApproved();
	}
	
	@PostMapping("/approve-agent/{pendingUserId}")
	public ResponseEntity<?> approvePendingAgent(@PathVariable(name="pendingUserId") Long id) {
		 try {
		        deliveryAgentService.approveAgentById(id);
		        return ResponseEntity.ok(Map.of("message", "Agent approved successfully"));
		    } catch (ResourceNotFoundException | RegistrationException | FileStorageException ex) {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", ex.getMessage()));
		    }
	}
	
	@PostMapping("/reject-agent/{pendingUserId}")
	public void rejectPendingAgent(@PathVariable(name="pendingUserId") Long id) {
		
	}
	
	@PostMapping(path="/addFoodCategory",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CategoryResponseDTO> addFoodCategory(@Valid @ModelAttribute  CreateFoodCategoryDTO createCategoryDTO) {
		CategoryResponseDTO categoryResponseDTO = foodCategoryService.addFoodCategory(createCategoryDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
	}
	
	@PostMapping(path="/addFoodItem",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FoodItemResponseDTO> addFoodItem(@ModelAttribute @Valid CreateFoodItemDTO foodItem) {	
		FoodItemResponseDTO foodItemResponseDTO = foodItemService.addFoodItem(foodItem);
		return ResponseEntity.status(HttpStatus.CREATED).body(foodItemResponseDTO);
	}
	
	@PostMapping(path="/addFoodVariant" )
	public ResponseEntity<FoodVariantResponseDTO> addFoodVariant(CreateFoodVariantDTO createFoodVariantDTO){
		FoodVariantResponseDTO res = foodVariantService.addFoodVariant(createFoodVariantDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
	@PutMapping(path="/foodCategory/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CategoryResponseDTO> updateFoodCategoryById(@PathVariable(name="id") Long id,@ModelAttribute UpdateFoodCategoryDTO category) {
		CategoryResponseDTO updated = foodCategoryService.updateFoodCategoryById(id,category);
		return ResponseEntity.ok().body(updated);
	}
	
	@PutMapping(path="/foodItem/{id}", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FoodItemResponseDTO> updateFoodItemById(@PathVariable(name="id") Long id,@ModelAttribute UpdateFoodItemDTO item) {
		FoodItemResponseDTO updated = foodItemService.updateFoodItemById(id,item);
		return ResponseEntity.ok().body(updated);
	}
	
	@PutMapping(path="foodVariant/{id}")
	public ResponseEntity<FoodVariantResponseDTO> updateFoodVariantById(@PathVariable(name="id") Long id, @RequestBody UpdateFoodVariantDTO variant){
		FoodVariantResponseDTO updated =  foodVariantService.updateFoodVariantById(id,variant);
		return ResponseEntity.ok().body(updated);
	}
	
	@DeleteMapping(path="/foodCategory/{id}")
	public ResponseEntity<?> deleteFoodCategoryById(@PathVariable(name="id") Long id){
		foodCategoryService.deleteFoodCategoryById(id);
		return ResponseEntity.noContent().build();
		}
	
	@DeleteMapping(path="/foodItem/{id}")
	public ResponseEntity<?> deleteFoodItemById(@PathVariable(name="id") Long id) {
		foodItemService.deleteFoodItemById(id);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(path="/foodVariant/{id}")
	public ResponseEntity<?> deleteFoodVariantById(@PathVariable(name="id") Long id){
		foodVariantService.deleteFoodVariantById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/test")
	public String testingPublic() {
		return "Testing purppose";
	}
	
}
