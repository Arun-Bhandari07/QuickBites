package com.QuickBites.app.DTO;

import java.util.List;

import com.QuickBites.app.entities.LocationInfo;
import com.QuickBites.app.enums.PaymentMethod;

import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlaceOrderRequestDTO {

	@NotNull
    @Size(min = 1, message = "At least one cart item is required")
    private List<@Valid CartItemRequestDTO> items;
	
	private String specialInstructions;
	
	@NotBlank
	private String phoneNumber;
	
	@Embedded
	LocationInfo locationInfo;

	private Long addressId; // nullable – only set if user picks a saved address
	
	@NotNull(message = "Payment method is required")
	private PaymentMethod paymentMethod;
	
	public List<CartItemRequestDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemRequestDTO> items) {
		this.items = items;
	}

	
	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocationInfo getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(LocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	
	
}
