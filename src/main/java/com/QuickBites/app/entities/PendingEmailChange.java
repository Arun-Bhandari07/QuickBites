package com.QuickBites.app.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pending_email_change")
public class PendingEmailChange {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(nullable = false)
	private int userId;

	@Column(nullable = false)
	private String newEmail;

	@Column(nullable = false, unique = true)
	private String otp;

	@Column(nullable = false)
	private LocalDateTime expiresAt;

	public PendingEmailChange() {
	}

	public PendingEmailChange(int userId, String newEmail, String otp) {
		this.userId = userId;
		this.newEmail = newEmail;
		this.otp = otp;
		this.expiresAt = LocalDateTime.now().plusMinutes(15); // A bit longer for email
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	
}
