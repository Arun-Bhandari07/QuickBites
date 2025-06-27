package com.QuickBites.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.CreateCategoryDTO;
import com.QuickBites.app.DTO.CreateFoodItemDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.services.FoodCategoryService;
import com.QuickBites.app.services.FoodItemService;

import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
	
	FoodCategoryService foodCategoryService;
	FoodItemService foodItemService;
	
	public AdminController(FoodCategoryService foodCategoryService
			,FoodItemService foodItemService) {
		this.foodCategoryService=foodCategoryService;
		this.foodItemService = foodItemService;
	}
	
	@GetMapping("/approveUser")
	public void approveUser() {}
	
	
	@PostMapping(path="/addFoodCategory",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CategoryResponseDTO> addFoodCategory(@Valid @ModelAttribute  CreateCategoryDTO createCategoryDTO) {
		CategoryResponseDTO categoryResponseDTO = foodCategoryService.addFoodCategory(createCategoryDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
	}
	
	@PostMapping(path="/addFoodItem",consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<FoodItemResponseDTO> addFoodItem(@ModelAttribute @Valid CreateFoodItemDTO foodItem) {	
		FoodItemResponseDTO foodItemResponseDTO = foodItemService.addFoodItem(foodItem);
		return ResponseEntity.status(HttpStatus.CREATED).body(foodItemResponseDTO);
	}
	
	
}
