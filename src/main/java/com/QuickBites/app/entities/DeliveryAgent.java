package com.QuickBites.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DeliveryAgent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String citizenshipPhotoFront;
	private String citizenshipPhotoBack;
	private String drivingLicense;
	
	@OneToOne
	@JoinColumn(name="_userId")
	User user;	
	
	public String getCitizenshipPhotoFront() {
		return citizenshipPhotoFront;
	}
	public void setCitizenshipPhotoFront(String citizenshipPhotoFront) {
		this.citizenshipPhotoFront = citizenshipPhotoFront;
	}
	public String getCitizenshipPhotoBack() {
		return citizenshipPhotoBack;
	}
	public void setCitizenshipPhotoBack(String citizenshipPhotoBack) {
		this.citizenshipPhotoBack = citizenshipPhotoBack;
	}
	
	
	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	
	
}
