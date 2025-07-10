package com.QuickBites.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.repositories.PendingUserRepository;

@Service
public class DeliveryAgentService {
	
	@Autowired
	private PendingUserRepository pendingUserRepo;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private MailService mailService;
	
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
	
	public void rejectAgentById(Long id, String reason) {
	    PendingUser pendingUser = pendingUserRepo.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));

	    if (!pendingUser.getRoleName().equals(RoleName.ROLE_DELIVERYAGENT)) {
	        throw new IllegalStateException("User is not a delivery agent");
	    }
	    try {
	        imageService.deleteImage(pendingUser.getCitizenshipPhotoFront(),ImageType.DELIVERYAGENTREQUEST);
	        imageService.deleteImage(pendingUser.getCitizenshipPhotoBack(),ImageType.DELIVERYAGENTREQUEST);
	        imageService.deleteImage(pendingUser.getDriverLicense(),ImageType.DELIVERYAGENTREQUEST);
	    } catch (Exception ex) {
	        throw new FileStorageException("Error deleting agent documents", ex);
	    }

	    pendingUserRepo.delete(pendingUser);
	    mailService.sendAgentRejectionEmail(pendingUser.getEmail(), reason);
	}
	
	
}
