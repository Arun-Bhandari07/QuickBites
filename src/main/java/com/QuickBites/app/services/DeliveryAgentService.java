package com.QuickBites.app.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.AgentRejectionRequest;
import com.QuickBites.app.DTO.AgentResponseDTO;
import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.DeliveryAgent;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.ReapplyToken;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.mapper.DeliveryAgentMapper;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.repositories.ReapplyTokenRepository;

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
	
	@Autowired
	private DeliveryAgentRepository deliveryAgentRepo;
	
	@Autowired
	private ReapplyTokenRepository reapplyTokenRepo;
	
	
	public List<AgentResponseDTO> getAllAgents() {
		List<DeliveryAgent> agents = deliveryAgentRepo.findAll();
		List<AgentResponseDTO> res = 	agents.stream()
			.map(agent->DeliveryAgentMapper.entityToDto(agent))
			.collect(Collectors.toList());
		return res;
			
	}

	public List<AgentResponseDTO> getAllAgentsByStatus(boolean status){
		List<DeliveryAgent> agents = deliveryAgentRepo.findByIsActive(status);
		List<AgentResponseDTO> res = 	agents.stream()
				.map(agent->DeliveryAgentMapper.entityToDto(agent))
				.collect(Collectors.toList());
		return res;
	}
	
	public boolean approveAgentById(Long id) {
		PendingUser pendingUser = pendingUserRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("User with given id"+id+"doesn't exists on pending"));
		pendingUser.setAdminApproved(true);
		pendingUser  = pendingUserRepo.save(pendingUser);
		String email = pendingUser.getEmail();
		userRegistrationService.registerAgent(email);
		return true;
	}
	
	public String rejectAgentById(Long id, AgentRejectionRequest rejectionRequest) {
	    PendingUser pendingUser = pendingUserRepo.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));

	    if (!pendingUser.getRoleName().equals(RoleName.ROLE_DELIVERYAGENT)) {
	        throw new IllegalStateException("User is not a delivery agent");
	    }
	    
	    String reason = rejectionRequest.getReason();
        
        if(rejectionRequest.isAllowReapply()) {
        	ReapplyToken token =new ReapplyToken(pendingUser.getId());
        	reapplyTokenRepo.save(token);
        	
        	//send the mail with the reapply link
            mailService.sendAgentRejectionWithReapplyLinkEmail(pendingUser.getEmail(), reason, token.getToken());
            return "Agent rejected but allowed to reapply. A reapplication link has been sent.";

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
	    return "Agent has been permanently rejected and their data has been deleted.";
	}
	
	public ResponseEntity<String> setOnline(Long id) {
		DeliveryAgent agent = deliveryAgentRepo.findById(id)
				.orElseThrow(()-> new ResourceNotFoundException("Cannot find delivery agent with id"+id));
		agent.setActive(true);
		agent.setLastSeen(LocalDateTime.now());
		deliveryAgentRepo.save(agent);
		return ResponseEntity.ok("Agent is now online");
	}
	
	public ResponseEntity<String> setOffline(Long id){
		DeliveryAgent agent = deliveryAgentRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find delivery agent with id"+id));
		agent.setActive(false);
		deliveryAgentRepo.save(agent);
		return ResponseEntity.ok("Delivery Agent marked offline");
		
	}
	
	public ResponseEntity<String> updateLastSeen(Long id){
		DeliveryAgent agent = deliveryAgentRepo.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Cannot find delivery agent with id"+id));
		agent.setLastSeen(LocalDateTime.now());
		deliveryAgentRepo.save(agent);
		return ResponseEntity.ok("Update Last Seen");
	}
	
	
}
