package com.QuickBites.app.DTO;

import com.QuickBites.app.enums.KitchenStatus;

import jakarta.validation.constraints.NotNull;

public class StatusUpdateDTO {

	@NotNull(message="Status must be provided")
	private KitchenStatus kitchenStatus;

	public KitchenStatus getKitchenStatus() {
		return kitchenStatus;
	}

	public void setKitchenStatus(KitchenStatus kitchenStatus) {
		this.kitchenStatus = kitchenStatus;
	}
	
	
	
}
