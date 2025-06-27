package com.QuickBites.app.DTO;

import java.math.BigDecimal;

import com.QuickBites.app.entities.FoodItem;

public class CreateFoodVariantDTO {

	private String name;
	
	private BigDecimal price;
	
	private FoodItem foodItem;

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

	public FoodItem getFoodItem() {
		return foodItem;
	}

	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}
	
}
