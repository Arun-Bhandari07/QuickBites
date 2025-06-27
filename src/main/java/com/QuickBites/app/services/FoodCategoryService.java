package com.QuickBites.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.CreateCategoryDTO;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.entities.FoodCategory;
import com.QuickBites.app.mapper.FoodCategoryMapper;
import com.QuickBites.app.repositories.FoodCategoryRepository;

@Service
public class FoodCategoryService {

	FoodCategoryRepository foodCategoryRepo;
	ImageService imageService;
	FoodCategoryMapper foodCategoryMapper;
	
	public FoodCategoryService(FoodCategoryRepository foodCategoryRepo
								,ImageService imageService
								,FoodCategoryMapper foodCategoryMapper) {
		this.foodCategoryRepo = foodCategoryRepo;
		this.imageService = imageService;
		this.foodCategoryMapper= foodCategoryMapper;
		
		
	}
	
	public CategoryResponseDTO addFoodCategory(CreateCategoryDTO createCategoryDTO) {
		if(foodCategoryRepo.existsByNameIgnoreCase(createCategoryDTO.getName().trim())) {
			throw new ResourceAlreadyExistsException("The given category already Exists");
		}
		FoodCategory category = foodCategoryMapper.categoryDTOToEntity(createCategoryDTO);
		category = foodCategoryRepo.save(category);
		CategoryResponseDTO categoryResponseDTO= foodCategoryMapper.entityToCategoryResponse(category);
		return categoryResponseDTO;
	}
	
	public List<CategoryResponseDTO> getFoodCategories(){
		List<FoodCategory> foodCategories = foodCategoryRepo.findAll();
		List<CategoryResponseDTO> response = foodCategories.stream().map(category->foodCategoryMapper.entityToCategoryResponse(category))
								.collect(Collectors.toList());
		return response;
	}
	
	
}
