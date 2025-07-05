package com.QuickBites.app.mapper;

import org.springframework.stereotype.Component;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.CreateFoodCategoryDTO;
import com.QuickBites.app.entities.FoodCategory;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.services.ImageService;

@Component
public class FoodCategoryMapper {
	

	
	private ImageService imageService;
	
	
	public FoodCategoryMapper(ImageService imageService) {
		this.imageService = imageService;
	}

	public  FoodCategory categoryDTOToEntity(CreateFoodCategoryDTO categoryRequest){
		FoodCategory foodCategory = new FoodCategory();
		foodCategory.setName(categoryRequest.getName().trim());
		foodCategory.setDescription(categoryRequest.getDescription());
		foodCategory.setActive(categoryRequest.getIsActive());
		foodCategory.setImageUrl(imageService.saveImage(categoryRequest.getImage(),ImageType.FOOD_CATEGORY));
		return foodCategory;	
	}
	
	public  CategoryResponseDTO entityToCategoryResponse(FoodCategory category) {
		CategoryResponseDTO  categoryResponseDTO = new CategoryResponseDTO();
		categoryResponseDTO.setName(category.getName());
		categoryResponseDTO.setId(category.getId());
		categoryResponseDTO.setActive(category.isActive());
		categoryResponseDTO.setDescription(category.getDescription());
		categoryResponseDTO.setImageUrl(category.getImageUrl());
		return categoryResponseDTO;
	}
	
	
}
