package com.QuickBites.app.DTO;

import jakarta.validation.constraints.NotNull;

//Class to get the final Order request from frontend 
public class CartItemRequestDTO {

	 @NotNull(message = "CartItemId cannot be null")
	    private Long cartItemId;

	public Long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}
	 	
}
