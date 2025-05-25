package com.QuickBites.app.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;

@Entity
public class OTPVerification {
	
	public  OTPVerification() {}
	
	@Transient
	private final int OTP_Validity_period = 5;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable=false)
	private int id;
	
	private String otp;
	
	private String email;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	private LocalDateTime expiryAt;
	

	public LocalDateTime getExpiryAt() {
		return expiryAt;
	}

	public void setExpiryAt(LocalDateTime expiryAt) {
		this.expiryAt = expiryAt;
	}

	@PrePersist
	public void setExpiration() {
		this.createdAt = LocalDateTime.now();
		this.expiryAt = this.createdAt.plusMinutes(OTP_Validity_period);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
}
