package com.QuickBites.app.Exception;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

public class ApiErrorResponse {
	
	private final String message;
	private final LocalDateTime time;
	private final int status;
	private final String path;
	private final String errorCode;
	private final Map<String,Object> fieldErrors;
	

	public ApiErrorResponse(String message, int status , String path, String errorCode) {
			this(message,status,path,errorCode,Collections.emptyMap());
	}
	
	public ApiErrorResponse(String message, int status , String path, String errorCode,Map<String,Object>fieldErrors) {
		this.message=message;
		this.status=status;
		this.path=path;
		this.errorCode=errorCode;
		this.time = LocalDateTime.now();
		this.fieldErrors = fieldErrors != null? fieldErrors:Collections.emptyMap();
	}
	
	public String getMessage() {
		return message;
	}

	

	public LocalDateTime getTime() {
		return time;
	}


	public int getStatus() {
		return status;
	}


	public String getPath() {
		return path;
	}

	
	public String getErrorCode() {
		return errorCode;
	}
	
	public Map<String,Object> getFieldErrors(){
		return fieldErrors;
	}


	
}
