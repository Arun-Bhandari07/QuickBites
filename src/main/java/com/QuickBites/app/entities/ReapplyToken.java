package com.QuickBites.app.entities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reapply_token")
public class ReapplyToken {
	@Id
	@Column(nullable = false, updatable = false)
	private String token;

	@Column(name = "pending_user_id", nullable = false)
	private Long pendingUserId;

	@Column(nullable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant expiresAt;

	@Column(nullable = false)
	private boolean used = false;
	
	public ReapplyToken() {}
	
	   public ReapplyToken(Long pendingUserId) {
	        this.token = UUID.randomUUID().toString();
	        this.pendingUserId = pendingUserId;
	        this.createdAt = Instant.now();
	        // Set expiration to 24 hours from creation
	        this.expiresAt = this.createdAt.plus(24, ChronoUnit.HOURS); 
	        this.used = false;
	    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getPendingUserId() {
		return pendingUserId;
	}

	public void setPendingUserId(Long pendingUserId) {
		this.pendingUserId = pendingUserId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
