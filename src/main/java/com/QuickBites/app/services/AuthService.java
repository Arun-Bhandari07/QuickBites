package com.QuickBites.app.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.QuickBites.app.DTO.AgentRegisterRequest;
import com.QuickBites.app.DTO.ApiResponse;
import com.QuickBites.app.DTO.CustomerRegisterRequest;
import com.QuickBites.app.DTO.LoginRequest;
import com.QuickBites.app.DTO.LoginResponse;
import com.QuickBites.app.configurations.SecurityConfig;
import com.QuickBites.app.entities.DeliveryAgent;
import com.QuickBites.app.entities.RoleName;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.entities.UserRole;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.UserRepository;
import com.QuickBites.app.repositories.UserRoleRepository;
import com.QuickBites.app.utilities.JWTUtilities;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AuthService {
	JWTUtilities jwtUtilities;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserRoleRepository userRoleRepo;
	
	@Autowired
	DeliveryAgentRepository agentRepo;
	
	@Autowired
	SecurityConfig config;
	
	public  AuthService(JWTUtilities jwtUtilities) {
		this.jwtUtilities = jwtUtilities;
	}

	public ApiResponse<LoginResponse> authenticateUser(LoginRequest loginRequest) {
		try {
			Authentication authObj = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			CustomUserDetails customUserDetails = (CustomUserDetails) authObj.getPrincipal();
			String jwtToken = jwtUtilities.generateToken(authObj);
			Optional<User> optUser = userRepo.findByUserName(customUserDetails.getUsername());
			User user = optUser.get();
			LoginResponse loginResponse = new LoginResponse(user.getId(),user.getUserName(),jwtToken,jwtUtilities.extractDate(jwtToken));
			Set<String> roles = customUserDetails.getAuthorities().stream().map(role->role.getAuthority().toString()).collect(Collectors.toSet());
			loginResponse.setRoles(roles);
			ApiResponse res = new ApiResponse("success","User logged in",loginResponse);
			return res;
		}catch(Exception e) {
			throw new UsernameNotFoundException("Cannot finde user with username:"+loginRequest.getUsername());
		}	
	}

	public void registerCustomer(CustomerRegisterRequest regReq) {
		if(userRepo.existsByUserName(regReq.getUserName())) {
			throw new RuntimeException("Username already Taken");
		}
		User user = populateUserFromRequest(regReq);
		Optional<UserRole>  optRole = userRoleRepo.findByRole(RoleName.ROLE_CUSTOMER); 
		user.getRoles().add(optRole.get());
		userRepo.save(user);
	}
	
	public void registerAgent(AgentRegisterRequest regReq) throws Exception{
		System.out.println("Received Username: " + new ObjectMapper().writeValueAsString(regReq.getUserName()));
		System.out.println("Recived Licesne Photo :"+new ObjectMapper().writeValueAsString(regReq.getDrivingLicense().getOriginalFilename()));

		System.out.println("Recived Citizenship Photo :"+new ObjectMapper().writeValueAsString(regReq.getCitizenshipPhoto().getOriginalFilename()));
		String citizenshipPhoto = "";
		String drivingLicense = "";
		if(userRepo.existsByUserName(regReq.getUserName())) {
			throw new RuntimeException("Username already Taken");
		}
		try {
		 citizenshipPhoto = saveFile(regReq.getCitizenshipPhoto());
		 drivingLicense = saveFile(regReq.getDrivingLicense());
		}catch(Exception e) {
			throw new RuntimeException("Error with image files");
		}
		User user = populateUserFromRequest(regReq);
		UserRole  customerRole = userRoleRepo.findByRole(RoleName.ROLE_CUSTOMER).get();
		UserRole  agentRole = userRoleRepo.findByRole(RoleName.ROLE_DELIVERYAGENT).get();
		
		user.getRoles().add(customerRole);
		user.getRoles().add(agentRole);
//		userRepo.save(user);
		
		DeliveryAgent agent = new DeliveryAgent();
		agent.setCitizenshipPhoto(citizenshipPhoto);
		agent.setDrivingLicense(drivingLicense);
		agent.setUser(user);
//		agentRepo.save(agent);
	}
	
	
	public String saveFile(MultipartFile file) throws Exception{
		final String  UPLOAD_DIR = "src/main/resources/static/uploads";
		if(file.isEmpty()) {
			throw new Exception("File is empty");
		}
		String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
		Path filePath = Paths.get(UPLOAD_DIR,fileName);
//		Files.createDirectories(filePath.getParent());
		Files.write(filePath, file.getBytes());
		return fileName;
	}
	
	public User populateUserFromRequest(CustomerRegisterRequest regReq) {
		User user = new User();
		user.setFirstName(regReq.getFirstName());
		user.setLastName(regReq.getLastName());
		user.setAddress(regReq.getAddress());
		user.setPassword(config.passwordEncoder().encode(regReq.getPassword()));
		user.setUserName(regReq.getUserName());
		return user;
	}
	
	
	
}
	
	

