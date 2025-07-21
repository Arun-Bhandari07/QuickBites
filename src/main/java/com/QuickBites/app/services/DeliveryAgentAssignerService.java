package com.QuickBites.app.services;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.AgentNotificationDTO;
import com.QuickBites.app.entities.DeliveryAgent;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.enums.DeliveryStatus;
import com.QuickBites.app.repositories.DeliveryAgentRepository;
import com.QuickBites.app.repositories.OrderRepository;

@Service
public class DeliveryAgentAssignerService {

	
	private final DeliveryAgentRepository agentRepo;
	private final OrderRepository orderRepo;
	private final SimpMessagingTemplate messagingTemplate;
	
	public DeliveryAgentAssignerService(DeliveryAgentRepository agentRepo, OrderRepository orderRepo, SimpMessagingTemplate messagingTemplate) {
        this.agentRepo = agentRepo;
        this.orderRepo = orderRepo;
        this.messagingTemplate = messagingTemplate;
    }
	
	public void tryAssignDeliveryAgent(Order order) {
		Optional<DeliveryAgent> optionalAgent = agentRepo.findFirstAvailableAgent();
		if(optionalAgent.isPresent()) {
			DeliveryAgent deliveryAgent = optionalAgent.get();
			deliveryAgent.setAvailable(false);
			order.setAssignedAgent(deliveryAgent);
			order.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
			agentRepo.save(deliveryAgent);
			orderRepo.save(order);
			messagingTemplate.convertAndSend("/topic/agent-"+deliveryAgent.getId(),new AgentNotificationDTO(order.getId()));
		}
		messagingTemplate.convertAndSend("/topic/staff","No Available Delivery Agent");
		scheduleRetry(order.getId());
	}
		
	public void scheduleRetry(Long orderId) {
			Executors.newSingleThreadScheduledExecutor().schedule(()->{
				Optional<Order> optionalOrder = orderRepo.findById(orderId);
				if(optionalOrder.isPresent()) {
				Order order = optionalOrder.get();
				if(order.getAssignedAgent()==null && order.getDeliveryStatus()!=DeliveryStatus.DELIVERED && order.getDeliveryStatus()!=DeliveryStatus.CANCELLED) {
					tryAssignDeliveryAgent(order);
				}
				}
			},10,TimeUnit.MINUTES);
	}
		
	


}
