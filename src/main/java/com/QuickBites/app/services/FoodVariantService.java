package com.QuickBites.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CreateFoodVariantDTO;
import com.QuickBites.app.DTO.FoodVariantResponseDTO;
import com.QuickBites.app.DTO.UpdateFoodVariantDTO;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.entities.FoodVariant;
import com.QuickBites.app.mapper.FoodVariantMapper;
import com.QuickBites.app.repositories.FoodItemRepository;
import com.QuickBites.app.repositories.FoodVariantRepository;

@Service
public class FoodVariantService {
	@Autowired
	FoodVariantMapper foodVariantMapper;
	
	@Autowired
	FoodVariantRepository foodVariantRepo;
	
	@Autowired
	FoodItemRepository foodItemRepo;

	public FoodVariantResponseDTO addFoodVariant(CreateFoodVariantDTO createFoodVariant) {
		if(foodVariantRepo.existsByNameAndFoodItemId(createFoodVariant.getName(), createFoodVariant.getFoodItemId())) {
			throw new ResourceAlreadyExistsException("Food Variant Already Present for given food Item");
		}
		FoodVariant variant =foodVariantMapper.foodVariantRequestToEntity(createFoodVariant);
		variant = foodVariantRepo.save(variant);
		FoodVariantResponseDTO res = foodVariantMapper.foodVariantToResponseDTO(variant);
		return res;
	}
	
	public FoodVariantResponseDTO updateFoodVariantById(Long id , UpdateFoodVariantDTO updateDTO) {
		FoodVariant variant = foodVariantRepo.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("Food Variant not found for id: " + id));

		    // Update fields only if new values are provided
		    if (updateDTO.getName() != null) {
		        variant.setName(updateDTO.getName());
		    }

		    if (updateDTO.getPrice() != null) {
		        variant.setPrice(updateDTO.getPrice());
		    }

		    // Optional: allow reassigning to another food item
		    if (updateDTO.getFoodItemId() != null) {
		        FoodItem foodItem = foodItemRepo.findById(updateDTO.getFoodItemId())
		            .orElseThrow(() -> new ResourceNotFoundException("Updation Problem:Food Item not found for id: " + updateDTO.getFoodItemId()));
		        variant.setFoodItem(foodItem);
		    }

		    // Save the updated variant
		    FoodVariant updated = foodVariantRepo.save(variant);

		    // Map to response DTO
		    return foodVariantMapper.foodVariantToResponseDTO(updated);
	}
	
	public void deleteFoodVariantById(Long id) {
		FoodVariant variant = foodVariantRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Deletion Failed: Cannot find FoodVariant with id: "+id));
		
		foodVariantRepo.deleteById(id);
	}
	
	
}
