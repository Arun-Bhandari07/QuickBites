package com.QuickBites.app.DTO;

import java.util.List;

public class PlaceOrderRequestDTO {

	private List<CartItemRequestDTO> items;

	public List<CartItemRequestDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemRequestDTO> items) {
		this.items = items;
	}
	
}
