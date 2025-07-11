package com.QuickBites.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CategoryResponseDTO;
import com.QuickBites.app.DTO.CreateFoodCategoryDTO;
import com.QuickBites.app.DTO.UpdateFoodCategoryDTO;
import com.QuickBites.app.Exception.FileHandlingException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.FoodCategory;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.mapper.FoodCategoryMapper;
import com.QuickBites.app.repositories.FoodCategoryRepository;
import com.QuickBites.app.repositories.FoodItemRepository;

import jakarta.transaction.Transactional;

@Service
public class FoodCategoryService {

	private FoodCategoryRepository foodCategoryRepo;
	private FoodCategoryMapper foodCategoryMapper;
	private ImageService imageService;
	private FoodItemRepository foodItemRepo;
	
	public FoodCategoryService(FoodCategoryRepository foodCategoryRepo
								,FoodCategoryMapper foodCategoryMapper
								,ImageService imageService
								,FoodItemRepository foodItemRepo) {
		this.foodCategoryRepo = foodCategoryRepo;
		this.foodCategoryMapper= foodCategoryMapper;	
		this.imageService = imageService;
		this.foodItemRepo = foodItemRepo;
	}
	
	
	public CategoryResponseDTO addFoodCategory(CreateFoodCategoryDTO createCategoryDTO) {
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
	
	public CategoryResponseDTO getFoodCategory(Long id) {
		FoodCategory category = foodCategoryRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find Food Category with id: "+id));
		CategoryResponseDTO res = foodCategoryMapper.entityToCategoryResponse(category);
		return res;
	}
	
	@Transactional
	public CategoryResponseDTO updateFoodCategoryById(Long id,UpdateFoodCategoryDTO updateCategory) {
		FoodCategory foodCategory = foodCategoryRepo.findById(id)
						.orElseThrow(()->new ResourceNotFoundException("Food Category Doesnt Exist for id:"+id));
		
		if(updateCategory.getName()!=null) foodCategory.setName(updateCategory.getName());
		if(updateCategory.getDescription()!=null) foodCategory.setDescription(updateCategory.getDescription());
		if(updateCategory.isActive()!=null) foodCategory.setActive(updateCategory.isActive());
		
		
		if(updateCategory.getImage()!=null && !updateCategory.getImage().isEmpty()) { 
			String newImageUrl = imageService.saveImage(updateCategory.getImage(), ImageType.FOOD_CATEGORY);
			try {
				if(foodCategory.getImageUrl()!=null) {
					imageService.deleteImage(foodCategory.getImageUrl(), ImageType.FOOD_CATEGORY);
					foodCategory.setImageUrl(newImageUrl);
				}	
				
			}catch(Exception e) {
				imageService.deleteImage(newImageUrl, ImageType.FOOD_CATEGORY);
				throw new FileHandlingException("Couldnt delete file: "+foodCategory.getImageUrl());
			}
		}
		foodCategory =foodCategoryRepo.save(foodCategory);
		CategoryResponseDTO res = foodCategoryMapper.entityToCategoryResponse(foodCategory);
		return res;
	}
	
	public void deleteFoodCategoryById(Long id) {
		FoodCategory category = foodCategoryRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find category with id"+id));
		
		if(foodItemRepo.existsByCategoryId(id))	{
			throw new IllegalStateException("Cannot delete FoodCategory cause foodItems depends upon it");
		}
		
		if(category.getImageUrl()!=null && !category.getImageUrl().isBlank()) {
			String imageUrl = category.getImageUrl();
				imageService.deleteImage(imageUrl, ImageType.FOOD_CATEGORY);	
		}
		foodCategoryRepo.deleteById(id);
	}
	
	
	@Transactional
	public CategoryResponseDTO activateCategory(Long id) {
		FoodCategory category = foodCategoryRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Cannot find food category with id: "+id));
		
		category.setActive(true);
		FoodCategory savedCategory=foodCategoryRepo.save(category);
		return foodCategoryMapper.entityToCategoryResponse(savedCategory);
	}
	
	@Transactional
	public CategoryResponseDTO deactivateCategory(Long id) {
		FoodCategory category =foodCategoryRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find food category with id:"+id));
		category.setActive(false);
		FoodCategory  savedCategory=foodCategoryRepo.save(category);
		return foodCategoryMapper.entityToCategoryResponse(savedCategory);
	}
	
	
}
