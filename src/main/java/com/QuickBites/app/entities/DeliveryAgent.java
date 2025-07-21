package com.QuickBites.app.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class DeliveryAgent {

	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private String citizenshipPhotoFront;
	
	@Column(nullable=false)
	private String citizenshipPhotoBack;
	
	@Column(nullable=false)
	private String drivingLicense;
	
	private BigDecimal totalEarning = BigDecimal.ZERO;
	
	@Column(nullable=false)
	private double distanceCoveredForDay= 0.0;
	
	@Column(nullable=false)
	private int totalOrdersForDay= 0;
	
	@Column(nullable=false)
	private boolean isAvailable;
	
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDrivingLicense() {
		return drivingLicense;
	}
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	
	public BigDecimal getTotalEarning() {
		return totalEarning;
	}
	public void setTotalEarning(BigDecimal totalEarning) {
		this.totalEarning = totalEarning;
	}
	public double getDistanceCoveredForDay() {
		return distanceCoveredForDay;
	}
	public void setDistanceCoveredForDay(double distanceCoveredForDay) {
		this.distanceCoveredForDay = distanceCoveredForDay;
	}
	public int getTotalOrdersForDay() {
		return totalOrdersForDay;
	}
	public void setTotalOrdersForDay(int totalOrdersForDay) {
		this.totalOrdersForDay = totalOrdersForDay;
	}
	
	
		
	
	
}
