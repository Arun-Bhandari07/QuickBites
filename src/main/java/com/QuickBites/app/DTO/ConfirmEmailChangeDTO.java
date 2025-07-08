package com.QuickBites.app.DTO;

import jakarta.validation.constraints.NotBlank;

public class ConfirmEmailChangeDTO {
	@NotBlank(message = "OTP cannot be blank")
	private String otp;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}
	
	
}
