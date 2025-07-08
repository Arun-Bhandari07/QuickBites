package com.QuickBites.app.DTO;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlaceOrderRequestDTO {

	@NotNull
    @Size(min = 1, message = "At least one cart item is required")
	private List< @Valid CartItemRequestDTO> items;
	private String deliveryAddress;
	private String specialInstructions;

	
	
	public List<CartItemRequestDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemRequestDTO> items) {
		this.items = items;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	
	
	
}
