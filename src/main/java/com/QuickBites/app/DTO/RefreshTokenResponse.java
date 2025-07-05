package com.QuickBites.app.DTO;

public class RefreshTokenResponse {
	private String accessToken;
	
	public RefreshTokenResponse(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	

}
