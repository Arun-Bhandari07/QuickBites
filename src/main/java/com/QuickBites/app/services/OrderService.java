package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CartItemRequestDTO;
import com.QuickBites.app.DTO.OrderItemResponseDTO;
import com.QuickBites.app.DTO.OrderResponseDTO;
import com.QuickBites.app.DTO.PlaceOrderRequestDTO;
import com.QuickBites.app.DTO.PlaceOrderResponse;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.entities.FoodVariant;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.entities.OrderItem;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.enums.OrderStatus;
import com.QuickBites.app.repositories.FoodItemRepository;
import com.QuickBites.app.repositories.FoodVariantRepository;
import com.QuickBites.app.repositories.OrderRepository;
import com.QuickBites.app.repositories.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final FoodItemRepository foodItemRepo;
    private final FoodVariantRepository foodVariantRepo;
    private final StripeService stripeService;

    
    public OrderService(OrderRepository orderRepo,
                        UserRepository userRepo,
                        FoodItemRepository foodItemRepo,
                        FoodVariantRepository foodVariantRepo,
                        StripeService stripeService) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.foodItemRepo = foodItemRepo;
        this.foodVariantRepo = foodVariantRepo;
        this.stripeService = stripeService;
    }

    public PlaceOrderResponse placeOrder(PlaceOrderRequestDTO req, String username) {
		//find User to relate the order to particular user
		User user = userRepo.findByUserName(username)
				.orElseThrow(()->new ResourceNotFoundException("User doesnt exists"));
		
		//Create a real Order entity and set payment status pending
		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING_PAYMENT);
		order.setCreatedAt(LocalDateTime.now());
		List<OrderItem> orderItems  = new ArrayList<>();
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		//loop through each cartItemRequest to map to orderItem and store in list
		for(CartItemRequestDTO cartItem: req.getItems()){
			FoodItem foodItem = foodItemRepo.findById(cartItem.getFoodItemId())
			        .orElseThrow(() -> new ResourceNotFoundException("Food item not found"));

			    FoodVariant variant = foodVariantRepo.findById(cartItem.getVariantId())
			        .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
			    
			    BigDecimal price = Optional.ofNullable(variant.getPrice())
			    	    .orElseThrow(() -> new IllegalStateException("Variant price cannot be null"));
			    
			    int quantity = cartItem.getQuantity();
			    BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
			    totalAmount = totalAmount.add(subtotal);
			    
			    OrderItem orderItem = new OrderItem();
			    orderItem.setOrder(order);
			    orderItem.setFoodItem(foodItem);
			    orderItem.setVariant(variant);
			    orderItem.setQuantity(quantity);
			    orderItem.setPriceAtPurchase(price);
			   
			    orderItems.add(orderItem);
		}
		
		//Set the OrderItem list and total Amount in Order Entity
		order.setItems(orderItems);
		order.setTotalAmount(totalAmount);
		
	//Save order to database
		Order savedOrder = orderRepo.save(order);

		//call make payment method of stripe Service
		String stripeUrl = stripeService.makePayment(savedOrder);
		
		return new PlaceOrderResponse(savedOrder.getId(),totalAmount, stripeUrl);
		
		
	}
    
    
    public Order getOrderByIdForUser(Long orderId, String username) {
        return orderRepo.findByIdAndUserUserName(orderId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
    
    
    public List<OrderResponseDTO> getOrdersForUser(String username) {
    	 List<Order> orders= orderRepo.findByUserUserName(username);
    	 List<OrderResponseDTO> dtoList = orders.stream()
    	            .map(order->convertToResponseDTO(order))
    	            .toList();
    	 return dtoList;
    }
    
    
    public OrderResponseDTO convertToResponseDTO(Order order) {
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream().map(item -> {
            String foodName = item.getFoodItem().getName();
            String variantName = item.getVariant() != null ? item.getVariant().getName() : null;

            return new OrderItemResponseDTO(
                item.getId(),
                foodName,
                variantName,
                item.getQuantity(),
                item.getPriceAtPurchase()
            );
        }).collect(Collectors.toList());

        return new OrderResponseDTO(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus().name(),
            order.getCreatedAt(),
            itemDTOs
        );
    }
    
    
    public String retryCheckout(Long orderId) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order is already paid or canceled");
        }

        return stripeService.makePayment(order);
    }

    
}

