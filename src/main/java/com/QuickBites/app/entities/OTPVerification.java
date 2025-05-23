package com.QuickBites.app.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OTPVerification {
	
	public  OTPVerification() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable=false)
	private int id;
	
	private int otp;
	
	private String email;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	private LocalDateTime expiryAt;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
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

	public LocalDateTime getExpiryAt() {
		return expiryAt;
	}

	public void setExpiryAt(LocalDateTime expiryAt) {
		this.expiryAt = expiryAt;
	}

	
	
	
}
