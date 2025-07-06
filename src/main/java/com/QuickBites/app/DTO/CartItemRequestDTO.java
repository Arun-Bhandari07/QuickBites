package com.QuickBites.app.DTO;
//Class to get the final Order request from frontend 
public class CartItemRequestDTO {

	 private Long foodItemId;
	    private Long variantId; // nullable
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
