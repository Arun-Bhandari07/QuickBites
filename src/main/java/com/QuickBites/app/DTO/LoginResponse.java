package com.QuickBites.app.DTO;

import java.util.Set;

public class LoginResponse {
	
	private int id;
	private String username;
	private String token;
	private Long expiresAt;
	private Set<String> roles;
	
	public Long getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Long expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public LoginResponse(int id, String username, String token,Long expiresAt) {
		this.id = id;
		this.username = username;
		this.token = token;
		this.expiresAt = expiresAt;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	public Long getTokenExpiration() {
		return this.expiresAt;
	}
	
	public void setTokenExpiration(Long expirationDate) {
		this.expiresAt = expirationDate;
	}
	
	
	
}
