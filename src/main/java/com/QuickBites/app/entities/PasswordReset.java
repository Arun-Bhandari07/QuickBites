package com.QuickBites.app.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="password_reset")
public class PasswordReset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private String hashedNewPassword;

    @Column(nullable = false, unique = true)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    public PasswordReset() {}
    
    public PasswordReset(int userId, String hashedNewPassword, String otp) {
        this.userId = userId;
        this.hashedNewPassword = hashedNewPassword;
        this.otp = otp;
        // Set expiration for 10 minutes from creation
        this.expiresAt = LocalDateTime.now().plusMinutes(5);
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

	public String getHashedNewPassword() {
		return hashedNewPassword;
	}

	public void setHashedNewPassword(String hashedNewPassword) {
		this.hashedNewPassword = hashedNewPassword;
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
