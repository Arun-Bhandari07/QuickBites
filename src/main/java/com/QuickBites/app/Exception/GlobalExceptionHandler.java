package com.QuickBites.app.Exception;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler{
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest req){
		ApiErrorResponse res = new ApiErrorResponse(
				"Requested Resource doesn't exists"
				,HttpStatus.NOT_FOUND.value()
				,req.getDescription(false).substring(4)
				,"RESOURCE NOT FOUND");
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest req){
		Map<String,Object> fieldError = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			fieldError.put(fieldName, errorMessage);
		});;
		String path = req.getDescription(false).substring(4);
		ApiErrorResponse res = new ApiErrorResponse(
				"Invalid field input"
				,HttpStatus.BAD_REQUEST.value()
				,path
				,"Please fill out all fields properly"
				,fieldError
				);		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);	
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest req){
		
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.NOT_FOUND.value()
				,req.getDescription(false)
				,"USER_NOT_FOUND");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest req){
		
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.UNAUTHORIZED.value()
				,req.getDescription(false).substring(4)
				,"INVALID_CREDENTIALS");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
	}
	
	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest req){
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.CONFLICT.value()
				,req.getDescription(false)
				,"RESOURCE_ALREADY_EXISTS");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
	}

	
	
	
	@ExceptionHandler(UserVerificationException.class)
	public ResponseEntity<ApiErrorResponse> handleUserVerificationException(UserVerificationException ex, WebRequest req){
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.CONFLICT.value()
				,req.getDescription(false)
				,"USER_VERIFICATION_PENDING");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
	}
	
	@ExceptionHandler(EmailMessagingException.class)
	public ResponseEntity<ApiErrorResponse> handleEmailMessagingException(EmailMessagingException ex, WebRequest req){
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.INTERNAL_SERVER_ERROR.value()
				,req.getDescription(false)
				,"EMAIL_SENDING_ERROR");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
	
	
	@ExceptionHandler(FileHandlingException.class)
	public ResponseEntity<ApiErrorResponse> handleFileException(FileHandlingException ex, WebRequest req){
		
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.BAD_REQUEST.value()
				,req.getDescription(false).substring(4)
				,"BAD_REQUEST");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ApiErrorResponse> handleMaxSizeExceedException(MaxUploadSizeExceededException ex, WebRequest req){
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.PAYLOAD_TOO_LARGE.value()
				,req.getDescription(false).substring(4)
				,"PAYLOAD_TOO_LARGE");
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(res);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleGenericException(Exception ex, WebRequest req){
		ApiErrorResponse res = new ApiErrorResponse(
				ex.getMessage()
				,HttpStatus.INTERNAL_SERVER_ERROR.value()
				,req.getDescription(false).substring(4)
				,"INTERNAL_SERVER_ERROR");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
	
}