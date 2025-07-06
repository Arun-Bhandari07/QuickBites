package com.QuickBites.app.DTO;


public class AddToCartRequest {

	private Long foodItemId;
	private Long foodVariantId;
	private int quantity;
	
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
