package com.QuickBites.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CreateFoodItemDTO;
import com.QuickBites.app.DTO.FoodItemResponseDTO;
import com.QuickBites.app.DTO.UpdateFoodItemDTO;
import com.QuickBites.app.Exception.FileHandlingException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.FoodCategory;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.mapper.FoodItemMapper;
import com.QuickBites.app.repositories.FoodCategoryRepository;
import com.QuickBites.app.repositories.FoodItemRepository;

import jakarta.transaction.Transactional;

@Service
public class FoodItemService {

	private FoodItemMapper foodItemMapper;
	
	private FoodItemRepository foodItemRepo;
	
	private ImageService imageService;
	
	private FoodCategoryRepository foodCategoryRepo;
	
	public FoodItemService(FoodItemRepository foodItemRepo
			,FoodItemMapper foodItemMapper
			,ImageService imageService) {
		this.foodItemMapper = foodItemMapper;
		this.foodItemRepo = foodItemRepo;
		this.imageService = imageService;
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
	
	public FoodItemResponseDTO getFoodItem(Long id) {
		FoodItem foodItem =  foodItemRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find Food Item with id: "+id));
		FoodItemResponseDTO res = foodItemMapper.entityToFoodItemResponse(foodItem);
		return res;
	}
	
	@Transactional
	public FoodItemResponseDTO updateFoodItemById(Long id,UpdateFoodItemDTO updateItem) {
		FoodItem foodItem = foodItemRepo.findById(id)
						.orElseThrow(()->new ResourceNotFoundException("Food Item Doesnt Exist for id:"+id));
		
		if(updateItem.getName()!=null) foodItem.setName(updateItem.getName());
		if(updateItem.getDescription()!=null) foodItem.setDescription(updateItem.getDescription());
		if(updateItem.isActive()!=null) foodItem.setIsActive(updateItem.isActive());
		if(updateItem.getFoodCategoryId()!=null) {
			FoodCategory category = foodCategoryRepo.findById(id)
									.orElseThrow(()->new ResourceNotFoundException("Cannot find category with id: "+id));
			foodItem.setCategory(category);
		}
		
		
		if(updateItem.getImage()!=null && !updateItem.getImage().isEmpty()) { 
			String newImageUrl = imageService.saveImage(updateItem.getImage(), ImageType.FOOD_ITEM);
			try {
				if(foodItem.getImageUrl()!=null) {
					imageService.deleteImage(foodItem.getImageUrl(), ImageType.FOOD_ITEM);
					foodItem.setImageUrl(newImageUrl);
				}	
				
			}catch(Exception e) {
				imageService.deleteImage(newImageUrl, ImageType.FOOD_ITEM);
				throw new FileHandlingException("Couldnt delete file: "+foodItem.getImageUrl());
			}
		}
		foodItem =foodItemRepo.save(foodItem);
		FoodItemResponseDTO res = foodItemMapper.entityToFoodItemResponse(foodItem);
		return res;
	}
	
	
	public void deleteFoodItemById(Long id) {
		FoodItem foodItem = foodItemRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find User with id"+id));
		String imageUrl = foodItem.getImageUrl();
		if(imageUrl!=null&& !foodItem.getImageUrl().isBlank()) {
			imageService.deleteImage(imageUrl, ImageType.FOOD_ITEM);
			}
		foodItemRepo.deleteById(id);
	}
	
}
