package com.QuickBites.app.DTO;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFoodItemDTO {

	@NotBlank(message="Food name should ")
	private String name;
	
	@NotNull(message="Price should be mentioned")
	@DecimalMin(value="0.0", inclusive=false, message="Price must be greater than 0")
	private BigDecimal price;
	
	@NotNull(message="Image must be provided")
	private MultipartFile image;
	
	@NotBlank(message="Description must be provided")
	private String description;
	
//	@NotBlank(message="Status must be provided")
	private boolean isActive;
	
	@NotNull(message="Please provide category of Item")
	private Long categoryId;

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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	
	
}
