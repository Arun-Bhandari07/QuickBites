package com.QuickBites.app.DTO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public class AgentRegisterRequest extends CustomerRegisterRequest{

	@NotBlank(message="CitizenshipPhoto must not be present")
	private MultipartFile citizenshipPhotoFront;
	
	@NotBlank(message="DrivingLicense must not be present")
	private MultipartFile drivingLicense;
	
	@NotBlank(message="CitizenshipPhoto must not be present")
	private MultipartFile citizenshipPhotoBack;
	
	public MultipartFile getCitizenshipPhotoFront() {
		return citizenshipPhotoFront;
	}
	public void setCitizenshipPhotoFront(MultipartFile citizenshipPhotoFront) {
		this.citizenshipPhotoFront = citizenshipPhotoFront;
	}
	public MultipartFile getCitizenshipPhotoBack() {
		return citizenshipPhotoBack;
	}
	public void setCitizenshipPhotoBack(MultipartFile citizenshipPhotoBack) {
		this.citizenshipPhotoBack = citizenshipPhotoBack;
	}
	
	public MultipartFile getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(MultipartFile drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	
	
}
