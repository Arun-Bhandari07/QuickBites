package com.QuickBites.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AgentRegisterRequest;
import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.CustomerRegisterRequest;
import com.QuickBites.app.DTO.LoginRequest;
import com.QuickBites.app.DTO.LoginResponse;
import com.QuickBites.app.services.AuthService;
import com.QuickBites.app.services.MailService;
import com.QuickBites.app.services.OTPService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthService authService;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	OTPService otpService;

	
	public AuthController(){}
	
	
	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody  LoginRequest loginRequestDTO){
		try {
			ApiResponse<LoginResponse> res = authService.authenticateUser(loginRequestDTO);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}catch(Exception e) {	
			ApiResponse<LoginResponse> res = new ApiResponse<LoginResponse>("failed","Invalid Username or Password",null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
		}	
	}
	

	@PostMapping("/signup/customer")
	public ResponseEntity<?> customerSignUp(@Valid @RequestBody CustomerRegisterRequest regReq) {
		try {
			authService.registerCustomer(regReq);
			return ResponseEntity.status(HttpStatus.OK).body("User registered Successfully");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	
	
	@PostMapping("/signup/agent")
	public ResponseEntity<?> agentSignUp(@ModelAttribute AgentRegisterRequest regReq) {
		try {
			authService.registerAgent(regReq);
			return ResponseEntity.status(HttpStatus.OK).body("User registered");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}	
	}
	
	@PostMapping("/resend-otp")
	public ResponseEntity<?> resendOTP(@RequestParam("email") String email){
		try {
				mailService.sendMail(email);
				return ResponseEntity.ok().body("OTP resent");
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody Map<String,String> payload){
		try {
			String email = payload.get("email");
			String otp = payload.get("otp");
			Boolean verified = otpService.verifyOTP(email, otp);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP verified");
		}
		catch(RuntimeException e ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
			
		}
	}
	
	
	
	
	
}
