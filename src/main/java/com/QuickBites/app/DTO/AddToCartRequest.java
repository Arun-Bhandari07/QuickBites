package com.QuickBites.app.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequest {

	@NotNull(message="Invalid addition of food Item with no id")
	private Long foodItemId;
	
	@NotNull(message="Invalid addition of food Variatn with no id")
	private Long foodVariantId;
	
	
	@Max(value = 10, message = "Cannot add more than 10 items in cart. Please contact us directly")
	private  int quantity;
	
	public Long getFoodItemId() {
		return foodItemId;
	}
	public void setFoodItemId(Long foodItemId) {
		this.foodItemId = foodItemId;
	}
	public Long getFoodVariantId() {
		return foodVariantId;
	}
	public void setFoodVariantId(Long foodVariantId) {
		this.foodVariantId = foodVariantId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
