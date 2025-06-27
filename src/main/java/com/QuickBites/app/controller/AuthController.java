package com.QuickBites.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import com.QuickBites.app.services.MailService;
import com.QuickBites.app.services.OTPService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
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
			ApiResponse<LoginResponse> res = authService.authenticateUser(loginRequestDTO);
			return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	

	@PostMapping("/signup/customer")
	public ResponseEntity<?> customerSignUp(@Valid @RequestBody CustomerRegisterRequest regReq) {
			authService.registerPendingUser(regReq);
			return ResponseEntity.status(HttpStatus.OK).body("OTP has been sent to email");
	
	}

	@PostMapping("/signup/agent")
	public ResponseEntity<?> agentSignUp(@Valid @ModelAttribute AgentRegisterRequest regReq) {
			authService.registerPendingUser(regReq);
			return ResponseEntity.status(HttpStatus.OK).body("OTP has been sent to email");
		
	}
	
	@PostMapping("/resend-otp")
	public ResponseEntity<?> resendOTP(@RequestBody Map<String , String > payload){
			String email = payload.get("email");
				mailService.sendMail(email);
				return ResponseEntity.ok().body("OTP resent");
		
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOTP(@RequestBody Map<String,String> payload){
			Boolean verified = otpService.verifyOTP(payload.get("email"),payload.get("otp"));
			
			return ResponseEntity.status(HttpStatus.ACCEPTED).body("OTP_VERIFIED");	
	}
	
	
	@PostMapping("/forgot-password") 
    public ResponseEntity<ApiResponse<String>> requestPasswordReset(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Email address is required.", null));
        }
           authService.initiatePasswordReset(email); 
            return ResponseEntity.ok(new ApiResponse<>("success", "If an account with this email address exists, a password reset OTP has been sent.", null));
    
    }
	

}
