package com.QuickBites.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CreateFoodItemDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.mapper.FoodItemMapper;
import com.QuickBites.app.repositories.FoodItemRepository;

@Service
public class FoodItemService {

	private FoodItemMapper foodItemMapper;
	
	private FoodItemRepository foodItemRepo;
	
	public FoodItemService(FoodItemRepository foodItemRepo
			,FoodItemMapper foodItemMapper) {
		this.foodItemMapper = foodItemMapper;
		this.foodItemRepo = foodItemRepo;
	}
	
	public FoodItemResponseDTO addFoodItem(CreateFoodItemDTO createFoodItemDTO) {
		if(foodItemRepo.existsByNameIgnoreCase(createFoodItemDTO.getName())) {
			throw new ResourceAlreadyExistsException("Food Item Already Exists");
		}
		FoodItem foodItem = foodItemMapper.foodItemDTOToEntity(createFoodItemDTO);
		foodItem = foodItemRepo.save(foodItem);
		FoodItemResponseDTO foodItemResponse = foodItemMapper.entityToFoodItemResponse(foodItem);
		return foodItemResponse;	
	}
	
	public List<FoodItemResponseDTO> getFoodItemsWithVariants(){
		List<FoodItem> foodItems = foodItemRepo.findAllWithVariants();
		List<FoodItemResponseDTO> res = foodItems.stream().map(foodItem->foodItemMapper.entityToFoodItemResponse(foodItem))
										.collect(Collectors.toList());
		return res;
		
	}
	
}
