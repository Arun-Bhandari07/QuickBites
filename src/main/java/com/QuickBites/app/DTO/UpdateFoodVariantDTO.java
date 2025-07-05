package com.QuickBites.app.DTO;

import java.math.BigDecimal;

public class UpdateFoodVariantDTO {
	
	private String name;
	
	private BigDecimal price;
	
	private Long foodItemId;

	

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

	public Long getFoodItemId() {
		return foodItemId;
	}

	public void setFoodItemId(Long foodItemId) {
		this.foodItemId = foodItemId;
	}
	
}
