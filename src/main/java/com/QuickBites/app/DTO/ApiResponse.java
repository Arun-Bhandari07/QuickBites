package com.QuickBites.app.DTO;

public class ApiResponse<T> {

	private String status;
	private T data; 	
	private String message;
	
	public ApiResponse(String status,String message, T data) {
		this.data = data;
		this.message = message;
		this.status=status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}
	
	public void setData(T token ) {
		this.data = token;
	}
	
	
}
