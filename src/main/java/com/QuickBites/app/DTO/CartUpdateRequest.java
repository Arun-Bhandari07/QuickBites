package com.QuickBites.app.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartUpdateRequest {
    
    @NotNull(message = "Cart item ID cannot be null")
    private Long cartItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    // Getters and setters
    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
