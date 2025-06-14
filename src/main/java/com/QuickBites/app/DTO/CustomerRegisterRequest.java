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
		
		@NotBlank(message="Password must not be empty")
		@Size(min=6, message="Password must be at least 6 characters")
		private String password;
		
		@NotBlank(message="Password must not be empty")
		private String email;
		
		private String phone;
		
		@NotBlank(message="Password must not be empty")
		private String address;
		
		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		
		
//		private String citizenshipPhoto;
//		
//		private String licensePhoto;
		
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
