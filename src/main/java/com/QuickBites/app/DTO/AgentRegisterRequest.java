package com.QuickBites.app.DTO;

import org.springframework.web.multipart.MultipartFile;

public class AgentRegisterRequest extends CustomerRegisterRequest{

	
	private MultipartFile citizenshipPhoto;
	
	private MultipartFile drivingLicense;
	
	
	public MultipartFile getCitizenshipPhoto() {
		return citizenshipPhoto;
	}
	public void setCitizenshipPhoto(MultipartFile citizenshipPhoto) {
		this.citizenshipPhoto = citizenshipPhoto;
	}
	public MultipartFile getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(MultipartFile drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	
	
}
