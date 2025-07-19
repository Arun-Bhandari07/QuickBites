package com.QuickBites.app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.ChangePasswordDTO;
import com.QuickBites.app.DTO.InitiateEmailChangeDTO;
import com.QuickBites.app.DTO.UpdateProfileDTO;
import com.QuickBites.app.DTO.UserProfileDTO;
import com.QuickBites.app.Exception.InvalidCredentialsException;
import com.QuickBites.app.Exception.OtpValidationException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.UserNotFoundException;
import com.QuickBites.app.entities.PasswordReset;
import com.QuickBites.app.entities.PendingEmailChange;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.mapper.UserMapper;
import com.QuickBites.app.repositories.PasswordResetRepository;
import com.QuickBites.app.repositories.PendingEmailChangeRepository;
import com.QuickBites.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordResetRepository passwordResetRepo;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;
	private final OTPService otpService;
	private final PendingEmailChangeRepository pendingEmailChangeRepo;

	public UserService(UserRepository userRepository, UserMapper userMapper, PasswordResetRepository passwordResetRepo,
			PasswordEncoder passwordEncoder, MailService mailService, OTPService otpService,
			PendingEmailChangeRepository pendingEmailChangeRepo) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordResetRepo = passwordResetRepo;
		this.passwordEncoder = passwordEncoder;
		this.mailService = mailService;
		this.otpService = otpService;
		this.pendingEmailChangeRepo = pendingEmailChangeRepo;
	}

	public UserProfileDTO getUserProfile(String username) {
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UserNotFoundException("User not found with username" + username));
		UserProfileDTO dto = userMapper.populateUserProfileDTO(user);
		return dto;

	}
	
	
	public List<UserProfileDTO> getAllUsers(){
			List<User> userList =userRepository.findAll();
			List<UserProfileDTO> allUsers = userList.stream()
									.map(user->{
									UserProfileDTO dto = new UserProfileDTO();
									dto.setFirstName(user.getFirstName());
									dto.setLastName(user.getLastName());
									dto.setEmail(user.getEmail());
									dto.setUsername(user.getUserName());
									dto.setPhone(user.getPhone());
									return dto;
									})
					.collect(Collectors.toList());
			return allUsers;
								
	}
	
	
	public void getActiveAgents() {
		
	}
	

	public UserProfileDTO updateUserProfile(String username, UpdateProfileDTO updateData) {
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

		user.setFirstName(updateData.getFirstName());
		user.setLastName(updateData.getLastName());
		user.setPhone(updateData.getPhone());
//		user.getAddress().addAll(updateData.getAddress());
		User updatedUser = userRepository.save(user);
		return userMapper.populateUserProfileDTO(updatedUser);

	}

	@Transactional
	public void initiatePasswordChange(String username, ChangePasswordDTO passwordData) {
		if (!passwordData.getNewPassword().equals(passwordData.getConfirmPassword())) {
			throw new IllegalArgumentException("New password and confirmation password do not match.");
		}

		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UserNotFoundException("User not found: " + username));

		if (!passwordEncoder.matches(passwordData.getCurrentPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Incorrect current password provided.");
		}

		passwordResetRepo.deleteByUserId(user.getId());

		String otp = otpService.generateOTP();
		String hashedNewPassword = passwordEncoder.encode(passwordData.getNewPassword());

		PasswordReset resetRequest = new PasswordReset(user.getId(), hashedNewPassword, otp);
		passwordResetRepo.save(resetRequest);

		mailService.sendPasswordChangeOtp(user.getEmail(), otp);
	}

	@Transactional
	public void confirmPasswordChange(String username, String otp) {
		User user = userRepository.findByUserName(username)
				.orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

		PasswordReset resetRequest = passwordResetRepo.findByUserIdAndOtp(user.getId(), otp)
				.orElseThrow(() -> new OtpValidationException("Invalid or incorrect OTP provided."));

		if (resetRequest.getExpiresAt().isBefore(LocalDateTime.now())) {
			passwordResetRepo.delete(resetRequest);
			throw new OtpValidationException("The OTP has expired. Please initiate the process again.");
		}

		user.setPassword(resetRequest.getHashedNewPassword());
		userRepository.save(user);

		passwordResetRepo.delete(resetRequest);
	}


	@Transactional
    public void initiateEmailChange(String username, InitiateEmailChangeDTO emailChangeData) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        String newEmail = emailChangeData.getNewEmail().toLowerCase().trim();

        if (userRepository.existsByEmail(newEmail)) {
            throw new ResourceAlreadyExistsException("This email address is already in use.");
        }
        
        pendingEmailChangeRepo.deleteByUserId(user.getId());

        // Generate OTP and create the pending record
        String otp = otpService.generateOTP();
        PendingEmailChange emailChangeRequest = new PendingEmailChange(user.getId(), newEmail, otp);
        pendingEmailChangeRepo.save(emailChangeRequest);

        // --- Look how clean this is now ---
        // Call the specific methods in MailService.
        mailService.sendEmailChangeVerificationOtp(newEmail, otp);
        mailService.sendEmailChangeAlert(user.getEmail()); // Send alert to the old email
    }


    // --- The 'confirm' method was already correct, but here it is for completeness ---
    @Transactional
    public void confirmEmailChange(String username, String otp) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        PendingEmailChange emailChangeRequest = pendingEmailChangeRepo.findByUserIdAndOtp(user.getId(), otp)
                .orElseThrow(() -> new OtpValidationException("Invalid or incorrect OTP provided."));

        if (emailChangeRequest.getExpiresAt().isBefore(LocalDateTime.now())) {
            pendingEmailChangeRepo.delete(emailChangeRequest);
            throw new OtpValidationException("The OTP has expired.Please initiate the process again.");
        }

        user.setEmail(emailChangeRequest.getNewEmail());
        userRepository.save(user);

        pendingEmailChangeRepo.delete(emailChangeRequest);
    }
}
