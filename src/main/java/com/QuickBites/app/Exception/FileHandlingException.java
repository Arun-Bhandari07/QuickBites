package com.QuickBites.app.Exception;

public class FileHandlingException extends RuntimeException {

	public FileHandlingException(String message, Throwable cause) {
		super(message,cause);
	}
	
	public FileHandlingException(String message) {
		super(message);
	}
}







