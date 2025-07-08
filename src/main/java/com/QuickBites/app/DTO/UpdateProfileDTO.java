package com.QuickBites.app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProfileDTO {
    @NotBlank(message = "First name cannot be blank")
	@Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
	private String firstName;

    @NotBlank(message = "Last name cannot be blank")
	@Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
	private String lastName;

    @NotBlank(message = "Phone number cannot be blank")
	private String phone;

    @NotBlank(message = "Address cannot be blank")
	private String address;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
