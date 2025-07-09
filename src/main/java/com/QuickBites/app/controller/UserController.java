package com.QuickBites.app.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.ChangePasswordDTO;
import com.QuickBites.app.DTO.ConfirmEmailChangeDTO;
import com.QuickBites.app.DTO.ConfirmPasswordChangeDTO;
import com.QuickBites.app.DTO.InitiateEmailChangeDTO;
import com.QuickBites.app.DTO.UpdateProfileDTO;
import com.QuickBites.app.DTO.UserProfileDTO;
import com.QuickBites.app.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name="User Operations" ,description="Get profile detail, update profile")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/get-profile-detail")
	public ResponseEntity<UserProfileDTO> getMyProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		UserProfileDTO userProfile = userService.getUserProfile(username);
		return ResponseEntity.ok(userProfile);
	}

	@PutMapping("/update-profile")
	public ResponseEntity<UserProfileDTO> updateMyProfile(@Valid @RequestBody UpdateProfileDTO updateData) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		UserProfileDTO updatedProfile = userService.updateUserProfile(username, updateData);
		return ResponseEntity.ok(updatedProfile);
	}

	@PostMapping("/profile/initiate-password-change")
	public ResponseEntity<ApiResponse<String>> initiateMyPasswordChange(
			@Valid @RequestBody ChangePasswordDTO passwordData) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		userService.initiatePasswordChange(username, passwordData);

		ApiResponse<String> response = new ApiResponse<>("success",
				"Validation successful. An OTP has been sent to your email to confirm the change.", null);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/profile/confirm-password-change")
	public ResponseEntity<ApiResponse<String>> confirmMyPasswordChange(
			@Valid @RequestBody ConfirmPasswordChangeDTO confirmRequest) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		userService.confirmPasswordChange(username, confirmRequest.getOtp());

		ApiResponse<String> response = new ApiResponse<>("success", "Password changed successfully.", null);
		return ResponseEntity.ok(response);
	}
	
	  @PostMapping("/profile/initiate-email-change")
	    public ResponseEntity<ApiResponse<String>> initiateMyEmailChange(
	            @Valid @RequestBody InitiateEmailChangeDTO emailChangeData) {
	        
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        userService.initiateEmailChange(username, emailChangeData);

	        ApiResponse<String> response = new ApiResponse<>("success",
	            "An OTP has been sent to your NEW email address to complete the change.", null);
	        return ResponseEntity.ok(response);
	    }
	  
	  @PostMapping("/profile/confirm-email-change")
	    public ResponseEntity<ApiResponse<String>> confirmMyEmailChange(
	            @Valid @RequestBody ConfirmEmailChangeDTO confirmRequest) {
	        
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        userService.confirmEmailChange(username, confirmRequest.getOtp());

	        ApiResponse<String> response = new ApiResponse<>("success", "Your email address has been successfully updated.", null);
	        return ResponseEntity.ok(response);
	    }
}
