package com.QuickBites.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.services.FoodCategoryService;
import com.QuickBites.app.services.FoodItemService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public")
@Tag(name="Public APIs",description="Read Food Menu")
public class PublicController {
	
	@Autowired
	FoodCategoryService foodCategoryService;
	
	@Autowired
	FoodItemService foodItemService;
	
	@Autowired
	PendingUserRepository pendingUserRepo;
	
	
	@GetMapping("/foodCategories")
	public ResponseEntity<List<CategoryResponseDTO>> getCategories(){
		return ResponseEntity.ok(foodCategoryService.getFoodCategories());
	}
	
	@GetMapping("/foodItems")
	public ResponseEntity<List<FoodItemResponseDTO>> getFoodItemsWithVariants(){
		return ResponseEntity.ok(foodItemService.getFoodItemsWithVariants());
	}
	
	@GetMapping("/foodCategories/{id}")
	public ResponseEntity<CategoryResponseDTO> getFoodCategory(@PathVariable("id") Long id){
		CategoryResponseDTO category = foodCategoryService.getFoodCategory(id);
		return ResponseEntity.ok(category);
	}
	
	@GetMapping("/foodItems/{id}")
	public ResponseEntity<FoodItemResponseDTO> getFoodItem(@PathVariable("id") Long id){
		FoodItemResponseDTO foodItem = foodItemService.getFoodItem(id);
		return ResponseEntity.ok(foodItem);
	}
	
	@GetMapping("/pending-agents")
	public List<PendingUser> getPendingAgents() {
		return pendingUserRepo.findByNotAdminApproved();
	}
	
	
	
	
}
