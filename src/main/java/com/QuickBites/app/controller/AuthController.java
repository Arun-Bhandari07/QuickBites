package com.QuickBites.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.AgentRegisterRequest;
import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.CustomerRegisterRequest;
import com.QuickBites.app.DTO.LoginRequest;
import com.QuickBites.app.DTO.LoginResponse;
import com.QuickBites.app.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthService authService;
	
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
	public ResponseEntity<?> customerSignUp( @Valid @RequestBody CustomerRegisterRequest regReq) {
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
	
	
	
	
	
}
