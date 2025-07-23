package com.QuickBites.app.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.QuickBites.app.UserExistence;
import com.QuickBites.app.DTO.AgentRegisterRequest;
import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.CustomerRegisterRequest;
import com.QuickBites.app.DTO.LoginRequest;
import com.QuickBites.app.DTO.LoginResponse;
import com.QuickBites.app.DTO.RefreshTokenRequest;
import com.QuickBites.app.DTO.RefreshTokenResponse;
import com.QuickBites.app.Exception.FileHandlingException;
import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.InvalidCredentialsException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.Exception.UserNotFoundException;
import com.QuickBites.app.configurations.SecurityConfig;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.repositories.TokenRepository;
import com.QuickBites.app.repositories.UserRepository;
import com.QuickBites.app.repositories.UserRoleRepository;
import com.QuickBites.app.utilities.JWTUtilities;

@Service
public class AuthService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	JWTUtilities jwtUtilities;

	private final FileService fileService;

	private AdminNotificationService adminNotificationService;

	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;

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
		Long startTime = System.currentTimeMillis();
		Authentication authObj;
		try {
			authObj = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			CustomUserDetails customUserDetails = (CustomUserDetails) authObj.getPrincipal();
			User user = userRepo.findByUserName(customUserDetails.getUsername())
					.orElseThrow(() -> new UserNotFoundException("User doesn't exists"));

			String accessToken = jwtUtilities.generateAccessToken(authObj);
			String refreshToken = jwtUtilities.generateRefreshToken(authObj);
			tokenRepository.storeToken(user.getUserName(), accessToken, refreshToken);
			
			LoginResponse loginResponse = new LoginResponse(user.getId(), user.getUserName(), accessToken,
					jwtUtilities.extractDate(accessToken),refreshToken,jwtUtilities.extractDate(refreshToken));
			Set<String> roles = customUserDetails.getAuthorities().stream().map(role -> role.getAuthority().toString())
					.collect(Collectors.toSet());
			loginResponse.setRoles(roles);

			ApiResponse res = new ApiResponse("success", "User logged in", loginResponse);
			System.out.println("Time in Auth Service: "+(System.currentTimeMillis()-startTime));
			return res;
		} catch (BadCredentialsException ex) {
			throw new InvalidCredentialsException("Invalid username or password");
		}
	}

	public void registerPendingUser(CustomerRegisterRequest req) {
		List<UserExistence> pendingConflicts = pendingUserRepo.findConflicts(req.getUserName(), req.getEmail());
		for (UserExistence conflict : pendingConflicts) {
		    if (req.getUserName().equals(conflict.getUserName())) {
		        throw new ResourceAlreadyExistsException("Username in registration process: " + req.getUserName());
		    }
		    if (req.getEmail().equals(conflict.getEmail())) {
		        throw new ResourceAlreadyExistsException("Email in registration process: " + req.getEmail());
		    }
		}
		
		List<UserExistence> userConflicts = userRepo.findConflicts(req.getUserName(), req.getEmail());
		for (UserExistence conflict : userConflicts) {
		    if (req.getUserName().equals(conflict.getUserName())) {
		        throw new ResourceAlreadyExistsException("Username already taken: " + req.getUserName());
		    }
		    if (req.getEmail().equals(conflict.getEmail())) {
		        throw new ResourceAlreadyExistsException("Email already registered: " + req.getEmail());
		    }
		}
	

		PendingUser user = new PendingUser(); // saves a temporary user before verifying otp
		user.setFirstName(req.getFirstName());
		user.setLastName(req.getLastName());
		user.setUserName(req.getUserName());
		user.setPassword(config.passwordEncoder().encode(req.getPassword()));
		user.setEmail(req.getEmail());
		user.setRoleName(RoleName.ROLE_CUSTOMER);

		if (req instanceof AgentRegisterRequest) { // check if the received req is of customer or agent dto
			String citizenshipPathFront = "";
			String citizenshipPathBack = "";
			String licensePath = "";
			AgentRegisterRequest agentReq = (AgentRegisterRequest) req;
			try {
				citizenshipPathFront = fileService.saveFile(agentReq.getCitizenshipPhotoFront(),ImageType.DELIVERYAGENTREQUEST);
				citizenshipPathBack = fileService.saveFile(agentReq.getCitizenshipPhotoBack(),ImageType.DELIVERYAGENTREQUEST);
				licensePath = fileService.saveFile(agentReq.getDrivingLicense(),ImageType.DELIVERYAGENTREQUEST);
			}catch(FileHandlingException ex) {
				logger.warn(ex.getMessage());
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
	
	public RefreshTokenResponse refreshToken(RefreshTokenRequest req) {
		//check if the refreshToken is valid
		if(!jwtUtilities.isRefreshToken(req.getRefreshToken())
				|| jwtUtilities.isTokenExpired(req.getRefreshToken())) {
			throw new InvalidCredentialsException("Invalid Refresh Token");
		};
		
		//check if the refresh TOken is blacklisted
		if(tokenRepository.isRefreshTokenBlackListed(req.getRefreshToken())) {
			throw new InvalidCredentialsException("BlackListed Refresh Token");
		}
		
		//if valid, extract the username from refreshToken and check if user Exists
		String username = jwtUtilities.extractUsername(req.getRefreshToken());
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		if(userDetails == null) {
			throw new ResourceNotFoundException("Cannot find user for new access Token generation");
		}
		
		//if user is not banned| if user is enabled
		if(!userDetails.isEnabled()) {
			throw new DisabledException("The user is disabled from accessing the system to generate new access Token");
		}
		
		String storedRefreshToken = tokenRepository.getRefreshKeyToken(username);
		if(storedRefreshToken==null || !storedRefreshToken.equals(req.getRefreshToken())) {
			throw new InvalidCredentialsException("Invalid Refresh Token");
		}
			
		//get UserDetails from database
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		
		//construct new AccessToken from userDetails
		String accessToken = jwtUtilities.generateAccessToken(auth);
		
		//remove previous accessToken and add new Access Token
		tokenRepository.removeAccessToken(username);
		
		//store new accessToken in redis
		tokenRepository.storeToken(username, accessToken, storedRefreshToken);
		
		//construct RefreshTokenResponse obj
		RefreshTokenResponse res = new RefreshTokenResponse(accessToken);
		
		return res;
		
	}
	
	
	public void logout() {
		Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if(authentication==null || !authentication.isAuthenticated()) {
		    throw new IllegalStateException("No authenticated user found for logout.");
		}
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof UserDetails)) {
		    throw new IllegalStateException("Principal is not an instance of UserDetails.");
		}
		UserDetails userDetails = (UserDetails) principal;
		tokenRepository.removeAllToken(userDetails.getUsername());
	}

}
