package com.QuickBites.app.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

	public class CustomerRegisterRequest {
		
		public CustomerRegisterRequest() {}
		
		@NotBlank(message="Firstname should be filled")
		@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Firstname must contain only letters and be at least 2 characters")
		private String firstName;
		
		@NotBlank(message="Lastname should be filled")
		@Pattern(regexp = "^[A-Za-z]{2,}$", message = "Lastname must contain only letters and be at least 2 characters")
		private String lastName;
		
		@NotBlank(message="Username must not be blank")
		@Size(min=3, message="Username must be at least 3 characters")
		private String userName;
		
		@NotBlank(message="Password must not be empty")
		@Size(min=8, message="Password must be at least 8 characters")
		private String password;
		
		@NotBlank(message="Password must not be empty")
		@Email(message = "Invalid email format")
		private String email;
		
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	
	
	
}
