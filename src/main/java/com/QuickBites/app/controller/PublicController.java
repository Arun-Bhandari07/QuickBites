package com.QuickBites.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.repositories.FoodCategoryRepository;
import com.QuickBites.app.repositories.FoodItemRepository;
import com.QuickBites.app.services.AuthService;
import com.QuickBites.app.services.FoodCategoryService;
import com.QuickBites.app.services.FoodItemService;

@RestController
@RequestMapping("/public")
public class PublicController {

	@Autowired
	AuthService authService;
	
	@Autowired
	FoodCategoryRepository foodCategoryRepo;
	
	@Autowired
	FoodItemRepository foodItemRepo;
	
	@Autowired
	FoodCategoryService foodCategoryService;
	
	@Autowired
	FoodItemService foodItemService;
	
	
	@GetMapping("/foodCategories")
	public ResponseEntity<List<CategoryResponseDTO>> getCategories(){
		return ResponseEntity.ok(foodCategoryService.getFoodCategories());
	}
	
	@GetMapping("/foodItems")
	public ResponseEntity<List<FoodItemResponseDTO>> getFoodItemsWithVariants(){
		return ResponseEntity.ok(foodItemService.getFoodItemsWithVariants());
	}
	
}
