package com.QuickBites.app.DTO;

import java.math.BigDecimal;
import java.util.List;

import com.QuickBites.app.entities.FoodCategory;
import com.QuickBites.app.entities.FoodVariant;

public class FoodItemResponseDTO {

	private Long id;
	
	private String name;
	
	private BigDecimal price;
	
	private String description;
	
	private String imageUrl;
	
	private FoodCategory category;
	
	private boolean isActive;
	
	private List<FoodVariantResponseDTO> foodVariants;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public FoodCategory getCategory() {
		return category;
	}

	public void setCategory(FoodCategory category) {
		this.category = category;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<FoodVariantResponseDTO> getFoodVariants() {
		return foodVariants;
	}

	public void setFoodVariants(List<FoodVariantResponseDTO> foodVariants) {
		this.foodVariants = foodVariants;
	}
	
	
	
}
