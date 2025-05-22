package com.QuickBites.app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.LoginRequest;
import com.QuickBites.app.DTO.LoginResponse;
import com.QuickBites.app.Exception.InvalidCredentialsException;
import com.QuickBites.app.services.AuthService;

@RestController
@RequestMapping("/public")
public class HomeController {

	@Autowired
	AuthService authService;
	
	@GetMapping("/")
	public ResponseEntity<String> Home() {
		return ResponseEntity.ok("Hello guys"); 
	}
	
}
