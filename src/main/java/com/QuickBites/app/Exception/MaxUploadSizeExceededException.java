package com.QuickBites.app.Exception;

public class MaxUploadSizeExceededException extends RuntimeException{
	public MaxUploadSizeExceededException(String message) {
		super(message);
	}
}
