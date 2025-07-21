package com.QuickBites.app.services;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.OtpValidationException;
import com.QuickBites.app.Exception.RegistrationException;
import com.QuickBites.app.Exception.ResourceAlreadyExistsException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.DeliveryAgent;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.repositories.UserRepository;
import com.QuickBites.app.repositories.UserRoleRepository;

import jakarta.transaction.Transactional;

@Service
public class UserRegistrationService {
	
	private PendingUserRepository pendingUserRepo;
	private UserRepository userRepo;
	private UserRoleRepository userRoleRepo;
	private AdminNotificationService adminNotificationService;
	private FileService fileService;
	private DeliveryAgentRepository agentRepo;
	private MailService mailService;
	
	
	
	public UserRegistrationService(PendingUserRepository pendingUserRepo,
								   UserRepository userRepo,
								   UserRoleRepository userRoleRepo,
								   FileService fileService,
								   DeliveryAgentRepository agentRepo, 
								   AdminNotificationService adminNotificationService,
								   MailService mailService) {
		this.pendingUserRepo = pendingUserRepo;
		this.userRepo = userRepo;
		this.userRoleRepo=userRoleRepo;
		this.adminNotificationService=adminNotificationService;
		this.fileService = fileService;
		this.agentRepo = agentRepo;
		this.mailService=mailService;
		
	}
	
	@Transactional
	public void registerCustomer(String email) {
		PendingUser pendingUser = validatePendingUser(email);
		pendingUser.setOtpVerified(true);
		User user = populateUser(pendingUser);
		try {
			user.getRoles().add(userRoleRepo.findByRole(RoleName.ROLE_CUSTOMER).get());			
		}catch(NoSuchElementException ex) {
			throw new ResourceNotFoundException("System Configuration Error:The role ROLE_CUSTOMER is not defined in the database.");

		}
		userRepo.save(user);
		pendingUserRepo.save(pendingUser);
		pendingUserRepo.delete(pendingUser);
		
	}

	@Transactional
	public void registerAgent(String email) {
		PendingUser pendingUser = validatePendingUser(email);
		
		if(!pendingUser.isAdminApproved() || !pendingUser.isOTPVerified()) {
			throw new RegistrationException("Cannot register agent: The user has not been approved by an admin or has not completed OTP verification.");	
			}
		
		if (userRepo.existsByEmail(email)) {
		    throw new ResourceAlreadyExistsException("Delivery agent already registered");
		}
		
		User user = populateUser(pendingUser);
		try {
			user.getRoles().add(userRoleRepo.findByRole(RoleName.ROLE_DELIVERYAGENT).get());	
			user.getRoles().add(userRoleRepo.findByRole(RoleName.ROLE_CUSTOMER).get());
			
		}catch(NoSuchElementException ex) {
			throw new ResourceNotFoundException("System Configuration Error: A required user role ('ROLE_DELIVERYAGENT' or 'ROLE_CUSTOMER') is not defined in the database");
		}
		
		try {
		fileService.moveFile(pendingUser.getCitizenshipPhotoFront());
		fileService.moveFile(pendingUser.getCitizenshipPhotoBack());
		fileService.moveFile(pendingUser.getDriverLicense());
		}
		catch(IOException ex) {
			throw new FileStorageException("Failed to move required documents during agent registration",ex);
		}
		
		DeliveryAgent agent = new DeliveryAgent();
		agent.setCitizenshipPhotoFront(pendingUser.getCitizenshipPhotoFront());
		agent.setCitizenshipPhotoBack(pendingUser.getCitizenshipPhotoBack());
		agent.setDrivingLicense(pendingUser.getDriverLicense());
		
		agent.setUser(user);
		
		agentRepo.save(agent);
		userRepo.save(user);
		pendingUserRepo.delete(pendingUser);
		mailService.sendAgentApprovalEmail(user.getEmail(),user.getUserName());
	}	
	
	
	public User populateUser(PendingUser pendingUser) {
		User user = new User();
		user.setFirstName(pendingUser.getFirstName());
		user.setLastName(pendingUser.getLastName());
		user.setUserName(pendingUser.getUserName());
		user.setEmail(pendingUser.getEmail());
		user.setPassword(pendingUser.getPassword());
//		user.setCreatedAt(pendingUser.getCreatedAt());
		return user;
	}
	
	public PendingUser validatePendingUser(String email) {
		PendingUser pendingUser  = pendingUserRepo.findByEmail(email)
				.orElseThrow(()->new ResourceNotFoundException("User not found"));
		
		if(userRepo.existsByEmail(email)) {
			throw new ResourceAlreadyExistsException("Email already registered");
		}
		return pendingUser;
	}
	
	public void notifyAdminForApproval(PendingUser pendingUser) {
		if(!pendingUser.isOTPVerified()) {
			throw new OtpValidationException("OTP not verified");
		}
		adminNotificationService.notifyAdmin(pendingUser);
	}
	
}
