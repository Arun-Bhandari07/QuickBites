package com.QuickBites.app.DTO;

import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.annotations.NotEmptyMultipart;

public class AgentRegisterRequest extends CustomerRegisterRequest{
	
	public AgentRegisterRequest() {}

	@NotEmptyMultipart(message = "Front part of citizenship must be attached")
	private MultipartFile citizenshipPhotoFront;
	
	@NotEmptyMultipart(message = "DrivingLicense must be attached")
	private MultipartFile drivingLicense;
	
	@NotEmptyMultipart(message = "Rear part of citizenship must be attached")
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
