package com.QuickBites.app.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.QuickBites.app.DTO.ApiResponse;

//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//	@ExceptionHandler(InvalidCredentialsException.class)
//	public ResponseEntity<LoginResponse> handleInvalidCredential(){
//		
//}
