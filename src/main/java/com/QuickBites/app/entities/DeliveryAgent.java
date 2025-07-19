package com.QuickBites.app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
	private Long id;
	
	@Column(nullable=false)
	private String citizenshipPhotoFront;
	
	@Column(nullable=false)
	private String citizenshipPhotoBack;
	
	@Column(nullable=false)
	private String drivingLicense;
	
	@Column(nullable=false)
	private boolean isActive;
	
	@Column(nullable=false)
	private boolean isAvailable = false;
	
	private LocalDateTime lastSeen;
	
	private BigDecimal totalEarning = BigDecimal.ZERO;
	
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
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public BigDecimal getTotalEarning() {
		return totalEarning;
	}
	public void setTotalEarning(BigDecimal totalEarning) {
		this.totalEarning = totalEarning;
	}
	public LocalDateTime getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(LocalDateTime lastSeen) {
		this.lastSeen = lastSeen;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
		
	
	
}
