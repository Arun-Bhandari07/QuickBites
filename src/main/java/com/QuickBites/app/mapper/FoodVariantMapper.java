package com.QuickBites.app.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.QuickBites.app.DTO.CreateFoodVariantDTO;
import com.QuickBites.app.DTO.FoodVariantResponseDTO;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.entities.FoodVariant;
import com.QuickBites.app.repositories.FoodItemRepository;

@Component
public class FoodVariantMapper {
	
	@Autowired
	FoodItemRepository foodItemRepo;

	public FoodVariantResponseDTO foodVariantToResponseDTO(FoodVariant foodVariant) {
		FoodVariantResponseDTO foodVariantResponse = new FoodVariantResponseDTO();
		foodVariantResponse.setId(foodVariant.getId());
		foodVariantResponse.setName(foodVariant.getName());
		foodVariantResponse.setPrice(foodVariant.getPrice());
		return foodVariantResponse;
	}
	
	
	public FoodVariant foodVariantRequestToEntity(CreateFoodVariantDTO createFoodVariant) {
		FoodItem foodItem = foodItemRepo.findById(createFoodVariant.getFoodItemId())
							.orElseThrow(()->new ResourceNotFoundException("Cannot find foodItem of id "+createFoodVariant.getFoodItemId() +"to add FoodVariant"));
		FoodVariant foodVariant = new FoodVariant();
		foodVariant.setName(createFoodVariant.getName());
		foodVariant.setPrice(createFoodVariant.getPrice());
		foodVariant.setFoodItem(foodItem);
		return foodVariant;
	}
	
}
