package com.QuickBites.app.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDTO {
	@NotBlank(message = "Current password must be provided")
	private String currentPassword;

	@NotBlank(message = "New password cannot be blank")
	@Size(min = 8, message = "New password must be at least 6 characters")
	private String newPassword;

	@NotBlank(message = "Please confirm your new password")
	private String confirmPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
