package com.QuickBites.app.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

//Class to get the final Order request from frontend 
public class CartItemRequestDTO {

		@NotNull(message="Food item ID is required")
	 	private Long foodItemId;
		
	    @NotNull(message = "Variant ID is required")
	    private Long variantId; 
	    
	    @Min(value = 1, message = "Quantity must be at least 1")
	    private int quantity;
	    
		public Long getFoodItemId() {
			return foodItemId;
		}
		public void setFoodItemId(Long foodItemId) {
			this.foodItemId = foodItemId;
		}
		public Long getVariantId() {
			return variantId;
		}
		public void setVariantId(Long variantId) {
			this.variantId = variantId;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
	    
	
}
