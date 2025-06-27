package com.QuickBites.app.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.AgentRegisterRequest;
import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.CustomerRegisterRequest;
import com.QuickBites.app.DTO.LoginRequest;
import com.QuickBites.app.DTO.LoginResponse;
import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.InvalidCredentialsException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.UserNotFoundException;
import com.QuickBites.app.Exception.UserVerificationException;
import com.QuickBites.app.configurations.SecurityConfig;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.repositories.UserRepository;
import com.QuickBites.app.repositories.UserRoleRepository;
import com.QuickBites.app.utilities.JWTUtilities;

@Service
public class AuthService {
	JWTUtilities jwtUtilities;

	private final FileService fileService;

	private AdminNotificationService adminNotificationService;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserRoleRepository userRoleRepo;

	@Autowired
	DeliveryAgentRepository agentRepo;

	@Autowired
	MailService mailService;

	@Autowired
	SecurityConfig config;

	@Autowired
	OTPService otpService;

	@Autowired
	PendingUserRepository pendingUserRepo;

	public AuthService(JWTUtilities jwtUtilities, FileService fileService,
			AdminNotificationService adminNotificationService) {
		this.jwtUtilities = jwtUtilities;
		this.fileService = fileService;
		this.adminNotificationService = adminNotificationService;
	}

	public ApiResponse<LoginResponse> authenticateUser(LoginRequest loginRequest) {
		Authentication authObj;
		try {
			authObj = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			CustomUserDetails customUserDetails = (CustomUserDetails) authObj.getPrincipal();
			User user = userRepo.findByUserName(customUserDetails.getUsername())
					.orElseThrow(() -> new UserNotFoundException("User doesn't exists"));

			String jwtToken = jwtUtilities.generateToken(authObj);
			LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUserName(), jwtToken,
					jwtUtilities.extractDate(jwtToken));
			Set<String> roles = customUserDetails.getAuthorities().stream().map(role -> role.getAuthority().toString())
					.collect(Collectors.toSet());
			loginResponse.setRoles(roles);

			ApiResponse res = new ApiResponse("success", "User logged in", loginResponse);
			return res;
		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Invalid username or password");
		}
	}

	public void registerPendingUser(CustomerRegisterRequest req) {
		if (userRepo.existsByUserName(req.getUserName()) || (userRepo.existsByEmail(req.getEmail()))) {
			throw new ResourceAlreadyExistsException("Username already Taken"+req.getUserName());
		}

		if (pendingUserRepo.existsByEmail(req.getEmail())) {
			throw new UserVerificationException("User verification in progress"+req.getEmail());
		}

		PendingUser user = new PendingUser(); // saves a temporary user before verifying otp
		user.setFirstName(req.getFirstName());
		user.setLastName(req.getLastName());
		user.setUserName(req.getUserName());
		user.setPassword(config.passwordEncoder().encode(req.getPassword()));
		user.setAddress(req.getAddress());
		user.setEmail(req.getEmail());
		user.setPhone(req.getPhone());
		user.setRoleName(RoleName.ROLE_CUSTOMER);

		if (req instanceof AgentRegisterRequest) { // check if the received req is of customer or agent dto
			String citizenshipPathFront = "";
			String citizenshipPathBack = "";
			String licensePath = "";
			AgentRegisterRequest agentReq = (AgentRegisterRequest) req;
			try {
				citizenshipPathFront = fileService.saveFile(agentReq.getCitizenshipPhotoFront());
				citizenshipPathBack = fileService.saveFile(agentReq.getCitizenshipPhotoBack());
				licensePath = fileService.saveFile(agentReq.getDrivingLicense());
			}catch(Exception ex) {
				throw new FileStorageException("Problem with storing File",ex);
			}
				
			user.setCitizenshipPhotoFront(citizenshipPathFront);
			user.setCitizenshipPhotoBack(citizenshipPathBack);
			user.setDriverLicense(licensePath);
			user.setRoleName(RoleName.ROLE_DELIVERYAGENT);
			adminNotificationService.notifyAdmin(user);
		}
	
		pendingUserRepo.save(user);
		mailService.sendMail(user.getEmail());
		
	}

	public boolean initiatePasswordReset(String email) {
		User user = userRepo.findByEmail(email)
				.orElseThrow(()-> new UserNotFoundException("Couldn't find user with email:"+email));
		
		String otp = otpService.generateOTP();
		otpService.saveOTP(otp, email);
		mailService.sendPasswordResetOtpEmail(email, otp);
		return true;
		
	}

	public boolean passwordReset(String email, String newPassword) {
		Optional<User> userOptional = userRepo.findByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			String encodedPassword = config.passwordEncoder().encode(newPassword);
			user.setPassword(encodedPassword);
			otpService.deleteOTPByEmail(email);
			userRepo.save(user);
			return true;
		}
		return false;
	}

}
