package com.QuickBites.app.DTO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public class AgentReapplyRequest {
    @NotBlank(message = "Username cannot be blank")
    private String userName;
    
    private MultipartFile citizenshipPhotoFront;
    private MultipartFile citizenshipPhotoBack;
    private MultipartFile drivingLicense;
    
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
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
