package com.QuickBites.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.repositories.PendingUserRepository;

@Service
public class DeliveryAgentService {
	
	@Autowired
	private PendingUserRepository pendingUserRepo;
	
	@Autowired
	private UserRegistrationService userRegistrationService;

	public boolean approveAgentById(Long id) {
		PendingUser pendingUser = pendingUserRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("User with given id"+id+"doesn't exists on pending"));
		pendingUser.setAdminApproved(true);
		pendingUser  = pendingUserRepo.save(pendingUser);
		String email = pendingUser.getEmail();
		userRegistrationService.registerAgent(email);
		return true;
	}
}
