package com.QuickBites.app.services;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.AgentDashboardDTO;
import com.QuickBites.app.DTO.AgentRejectionRequest;
import com.QuickBites.app.DTO.AgentResponseDTO;
import com.QuickBites.app.DTO.DeliveryHistoryDTO;
import com.QuickBites.app.DTO.OrderItemResponseDTO;
import com.QuickBites.app.Exception.FileStorageException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.DeliveryAgent;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.entities.OrderItem;
import com.QuickBites.app.entities.PendingUser;
import com.QuickBites.app.entities.ReapplyToken;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.enums.DeliveryStatus;
import com.QuickBites.app.enums.ImageType;
import com.QuickBites.app.enums.PaymentMethod;
import com.QuickBites.app.enums.RoleName;
import com.QuickBites.app.mapper.DeliveryAgentMapper;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.OrderRepository;
import com.QuickBites.app.repositories.PendingUserRepository;
import com.QuickBites.app.repositories.ReapplyTokenRepository;
import com.QuickBites.app.repositories.UserRepository;

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
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	private static final DeliveryStatus COMPLETED_DELIVERY_STATUS = DeliveryStatus.DELIVERED;
	private static final PaymentMethod COD_PAYMENT_METHOD = PaymentMethod.COD;
	
	
	public List<AgentResponseDTO> getAllAgents() {
		List<DeliveryAgent> agents = deliveryAgentRepo.findAll();
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
	
	public AgentDashboardDTO getAgentDashboardData(String username) {
		User user = userRepo.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

		DeliveryAgent deliveryAgent = deliveryAgentRepo.findByUserId(user.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Delivery agent profile not found for user ID: " + user.getId()));

		Long agentId = deliveryAgent.getId();

		List<DeliveryStatus> excludedStatuses = List.of(DeliveryStatus.DELIVERED, DeliveryStatus.CANCELLED);

		long activeDeliveriesCount = orderRepo.countByAssignedAgentIdAndDeliveryStatusNotIn(agentId,
				excludedStatuses);

		BigDecimal totalEarningsToday = orderRepo.sumTotalEarningsTodayForAgent(agentId,
				COMPLETED_DELIVERY_STATUS, COD_PAYMENT_METHOD);

		long completedDeliveriesToday = orderRepo.countCompletedDeliveriesTodayForAgent(agentId,
				COMPLETED_DELIVERY_STATUS);

		long assignedDeliveriesToday = orderRepo.countAssignedDeliveriesTodayForAgent(agentId);

		return new AgentDashboardDTO(activeDeliveriesCount, totalEarningsToday, completedDeliveriesToday,
				assignedDeliveriesToday);
	}

	public List<DeliveryHistoryDTO> getAgentDeliveryHistory(String username) {
		DeliveryAgent agent = getAgentFromUsername(username);
		List<Order> completedOrders = orderRepo
				.findByAssignedAgentIdAndDeliveryStatusOrderByCreatedAtDesc(agent.getId(), COMPLETED_DELIVERY_STATUS);
		
		return completedOrders.stream().map(this::convertToDeliveryHistoryDTO).collect(Collectors.toList());
	}

	private DeliveryAgent getAgentFromUsername(String username) {
		User user = userRepo.findByUserName(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

		return deliveryAgentRepo.findByUserId(user.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Delivery agent profile not found for user ID: " + user.getId()));
	}
	
	  private DeliveryHistoryDTO convertToDeliveryHistoryDTO(Order order) {
	        String customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
	        String address = order.getLocationInfo().getDeliveryAddress();
	        
	        // Convert the list of OrderItem entities to a list of OrderItemResponseDTOs
	        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
	                .map(this::convertOrderItemToDTO)
	                .collect(Collectors.toList());

	        return new DeliveryHistoryDTO(
	                order.getId(),
	                customerName,
	                address,
	                order.getCreatedAt(),
	                order.getTotalAmount(),
	                order.getPaymentMethod(),
	                order.getDeliveryStatus(),
	                itemDTOs
	        );
	    }
	  
	  private OrderItemResponseDTO convertOrderItemToDTO(OrderItem item) {
	        return new OrderItemResponseDTO(
	            item.getId(),
	            item.getFoodItem().getName(),
	            item.getVariant().getName(),
	            item.getQuantity(),
	            item.getPriceAtPurchase()
	        );
	    }
	
	
}
