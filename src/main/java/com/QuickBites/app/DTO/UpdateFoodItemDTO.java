package com.QuickBites.app.DTO;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

public class UpdateFoodItemDTO {
	private Long id;
	private String name;
	private BigDecimal price;
	private MultipartFile image;
	private Boolean isActive;
	private Long foodCategoryId;
	private String description;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
	public MultipartFile getImage() {
		return image;
	}
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	public Boolean isActive() {
		return isActive;
	}
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public Long getFoodCategoryId() {
		return foodCategoryId;
	}
	public void setFoodCategoryId(Long foodCategoryId) {
		this.foodCategoryId = foodCategoryId;
	}
	

}
