package com.QuickBites.app.DTO;

import java.util.Set;

public class LoginResponse {
	
	private int id;
	private String username;
	private String accessToken;
	private String refreshToken;
	private Long accessTokenExpiry;
	private Long refreshTokenExpiry;
	private Set<String> roles;
	
	public LoginResponse(int id, String username, String accessToken,Long accessTokenExpiry,
			String refreshToken, Long refreshTokenExpiry) {
		this.id = id;
		this.username = username;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiry = accessTokenExpiry;
		this.refreshTokenExpiry = refreshTokenExpiry;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getAccessTokenExpiry() {
		return accessTokenExpiry;
	}

	public void setAccessTokenExpiry(Long accessTokenExpiry) {
		this.accessTokenExpiry = accessTokenExpiry;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
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

	public Long getRefreshTokenExpiry() {
		return refreshTokenExpiry;
	}

	public void setRefreshTokenExpiry(Long refreshTokenExpiry) {
		this.refreshTokenExpiry = refreshTokenExpiry;
	}

	
	
	
	
}
