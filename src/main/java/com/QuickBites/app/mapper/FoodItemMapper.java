package com.QuickBites.app.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.QuickBites.app.DTO.CreateFoodItemDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.DTO.FoodVariantResponseDTO;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.FoodCategory;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.entities.FoodVariant;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.repositories.FoodCategoryRepository;
import com.QuickBites.app.services.ImageService;

@Component
public class FoodItemMapper {
	private ImageService imageService;
	private FoodCategoryRepository foodCategoryRepo;
	private FoodVariantMapper foodVariantMapper;
	
	
	public FoodItemMapper(ImageService imageService
			,FoodCategoryRepository foodCategoryRepo
			,FoodVariantMapper foodVariantMapper) {
		this.imageService = imageService;
		this.foodCategoryRepo=foodCategoryRepo;
		this.foodVariantMapper=foodVariantMapper;
	}

	public FoodItem foodItemDTOToEntity(CreateFoodItemDTO createFoodItemDto) {
		FoodItem foodItem = new FoodItem();
		FoodCategory category = foodCategoryRepo.findById(createFoodItemDto.getCategoryId())
				.orElseThrow(()-> new ResourceNotFoundException("Invalid Category"));	
		foodItem.setName(createFoodItemDto.getName());
		foodItem.setCategory(category);
		foodItem.setDescription(createFoodItemDto.getDescription());
		foodItem.setPrice(createFoodItemDto.getPrice());
		foodItem.setIsActive(true);
		foodItem.setImageUrl(imageService.saveImage(createFoodItemDto.getImage(), ImageType.FOOD_ITEM));
		return foodItem;
	}
	
	public FoodItemResponseDTO entityToFoodItemResponse(FoodItem item) {
		FoodItemResponseDTO responseDTO = new FoodItemResponseDTO();
		responseDTO.setId(item.getId());
		responseDTO.setName(item.getName());
		responseDTO.setCategory(item.getCategory());
		responseDTO.setPrice(item.getPrice());
		responseDTO.setDescription(item.getDescription());
		responseDTO.setImageUrl(item.getImageUrl());
		responseDTO.setActive(item.getIsActive());
		
		List<FoodVariant> foodVariants = Optional.ofNullable(item.getFoodVariants()).orElse(Collections.emptyList());
		List<FoodVariantResponseDTO> variantResponseDTO = foodVariants.stream()
								.map(variant->foodVariantMapper.foodVariantToResponseDTO(variant))
								.collect(Collectors.toList());
		
		responseDTO.setFoodVariants(variantResponseDTO);
		return responseDTO;
	}
	
	
	
	
}
