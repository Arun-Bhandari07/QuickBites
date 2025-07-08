package com.QuickBites.app.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class InitiateEmailChangeDTO {
    @NotBlank(message = "New email address cannot be blank")
    @Email(message = "Please provide a valid email address")
    private String newEmail;


	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}
    
}
