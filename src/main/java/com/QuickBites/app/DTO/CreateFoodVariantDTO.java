package com.QuickBites.app.DTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateFoodVariantDTO {

	@NotBlank(message="Please fill Name")
	private String name;
	
	@NotNull(message="Price is needed")
	@DecimalMin(value="0.01" ,message="Price must be greater than 0")	
	private BigDecimal price;
	
	@NotNull(message="Select one of the foodItem")
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
