package com.QuickBites.app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequest {
	
	@NotBlank(message="Username should not be blank")
	@Size(min=3, max=30, message= "Username should be between 3 and 30 characters")
	private String username;
	
	@NotBlank(message="Password field should not be blank")
	@Size(min=6, message="Password should be minimum 6 characters")
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
