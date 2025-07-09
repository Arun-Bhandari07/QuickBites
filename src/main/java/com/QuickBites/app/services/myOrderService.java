package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.QuickBites.app.DTO.CartItemRequestDTO;
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
public class myOrderService {

	@Autowired
	UserRepository userRepo;
	@Autowired
	FoodVariantRepository foodVariantRepo;
	@Autowired
	FoodItemRepository foodItemRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	@Autowired
	StripeService stripeService;
	
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
			        .orElseThrow(() -> new RuntimeException("Food item not found"));

			    FoodVariant variant = foodVariantRepo.findById(cartItem.getVariantId())
			        .orElseThrow(() -> new RuntimeException("Variant not found"));
			    
			    BigDecimal price = variant.getPrice();
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
	
}
