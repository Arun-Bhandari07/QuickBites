package com.QuickBites.app.mapper;

import org.springframework.stereotype.Component;

import com.QuickBites.app.DTO.CreateFoodVariantDTO;
import com.QuickBites.app.DTO.FoodVariantResponseDTO;
import com.QuickBites.app.entities.FoodVariant;

@Component
public class FoodVariantMapper {

	public FoodVariantResponseDTO foodVariantToResponseDTO(FoodVariant foodVariant) {
		FoodVariantResponseDTO foodVariantResponse = new FoodVariantResponseDTO();
		foodVariantResponse.setId(foodVariant.getId());
		foodVariantResponse.setName(foodVariant.getName());
		foodVariantResponse.setPrice(foodVariant.getPrice());
		return foodVariantResponse;
	}
	
	
	public FoodVariant foodVariantRequestToEntity(CreateFoodVariantDTO createFoodVariant) {
		FoodVariant foodVariant = new FoodVariant();
		foodVariant.setName(createFoodVariant.getName());
		foodVariant.setPrice(createFoodVariant.getPrice());
		foodVariant.setFoodItem(createFoodVariant.getFoodItem());
		return foodVariant;
	}
	
}
