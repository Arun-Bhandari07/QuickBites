package com.QuickBites.app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

	public class CustomerRegisterRequest {
		
		@NotBlank(message="Firstname should be filled")
		private String firstName;
		
		@NotBlank(message="Lastname should be filled")
		private String lastName;
		
		@NotBlank
		@Size(min=3, message="Username must at least 3 characters")
		private String userName;
		
		@NotBlank
		private String password;
		
		@NotBlank
		private String email;
		
		
		@NotBlank
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	
	
	
}
