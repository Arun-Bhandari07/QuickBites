package com.QuickBites.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.OrderResponseDTO;
import com.QuickBites.app.DTO.StatusUpdateDTO;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.enums.KitchenStatus;
import com.QuickBites.app.repositories.OrderRepository;

@Service
public class StaffService {
	
	private final OrderRepository orderRepo;
	private final OrderService orderService;
	private final DeliveryAgentAssignerService deliveryAgentAssignerService;
	
	public StaffService(OrderRepository orderRepo,OrderService orderService,DeliveryAgentAssignerService deliveryAgentAssignerService) {
		this.orderRepo = orderRepo;
		this.orderService = orderService;
		this.deliveryAgentAssignerService=deliveryAgentAssignerService;
	}

	public List<OrderResponseDTO> getOrderForKitchenDashboard(){
		List<Order> orders = orderRepo.findOrderForKitchenDashboard();
		List<OrderResponseDTO> res = orders.stream()
							.map(
								order -> orderService.convertToResponseDTO(order)
								).collect(Collectors.toList());
		return res;
	}
	
	public void updateKitchenStatus(Long id, StatusUpdateDTO dto) {
			Order order = orderRepo.findById(id)
								.orElseThrow(()->new ResourceNotFoundException("Order with "+id+"doesn't exists"));
			order.setKitchenStatus(dto.getKitchenStatus());
			
			if(dto.getKitchenStatus() == KitchenStatus.READY && order.getAssignedAgent()== null) {
				deliveryAgentAssignerService.tryAssignDeliveryAgent(order);
			}
			orderRepo.save(order);
	
	}
	
	
}
