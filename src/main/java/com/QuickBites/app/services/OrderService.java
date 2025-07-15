package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.QuickBites.app.AdminOrderMetrics;
import com.QuickBites.app.DTO.AdminOrderMetricsDTO;
import com.QuickBites.app.DTO.CartItemRequestDTO;
import com.QuickBites.app.DTO.OrderItemResponseDTO;
import com.QuickBites.app.DTO.OrderResponseDTO;
import com.QuickBites.app.DTO.PlaceOrderRequestDTO;
import com.QuickBites.app.DTO.PlaceOrderResponse;
import com.QuickBites.app.Exception.BadRequestException;
import com.QuickBites.app.Exception.ResourceNotFoundException;
import com.QuickBites.app.entities.Address;
import com.QuickBites.app.entities.CartItem;
import com.QuickBites.app.entities.FoodItem;
import com.QuickBites.app.entities.FoodVariant;
import com.QuickBites.app.entities.LocationInfo;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.entities.OrderItem;
import com.QuickBites.app.entities.User;
import com.QuickBites.app.enums.OrderStatus;
import com.QuickBites.app.enums.PaymentMethod;
import com.QuickBites.app.repositories.AddressRepository;
import com.QuickBites.app.repositories.CartItemRepository;
import com.QuickBites.app.repositories.OrderRepository;
import com.QuickBites.app.repositories.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final StripeService stripeService;
    private final CartItemRepository cartItemRepo;
    private final AddressRepository addressRepo;

    
    public OrderService(OrderRepository orderRepo,
                        UserRepository userRepo,     
                        StripeService stripeService,
                        CartItemRepository cartItemRepo
                        ,AddressRepository addressRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.stripeService = stripeService;
        this.cartItemRepo=cartItemRepo;
        this.addressRepo=addressRepo;
    }

    public PlaceOrderResponse placeOrder(PlaceOrderRequestDTO req, String username){
    	LocationInfo location;
    	
    	
        User user = userRepo.findByUserName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if(user.getTrustScore() == 0 && req.getPaymentMethod() == PaymentMethod.COD) {
        	throw new BadRequestException("User cannot order via COD due to too much cancellation");
        }

        if (user.getPhone() == null && req.getPhoneNumber() != null) {
            user.setPhone(req.getPhoneNumber());   
            userRepo.save(user); // Persist updated phone
        }
        
        if (req.getAddressId() == null && req.getLocationInfo() == null) {
            throw new BadRequestException("Either addressId or custom location must be provided.");
        }
        
        if (req.getAddressId() != null) {
            Address address = addressRepo.findByIdAndUserId(req.getAddressId(), user.getId())
                .orElseThrow(() -> new AccessDeniedException("Address not found or not owned by user"));

            location = new LocationInfo();
            location.setLatitude(address.getLatitude());
            location.setLongitude(address.getLongitude());
            location.setDeliveryAddress(address.getFullAddress());
        } else {
            location = req.getLocationInfo(); // use manual input
        }
        
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
        order.setSpecialInstructions(req.getSpecialInstructions());
        order.setLocationInfo(location);
        order.setPhone(req.getPhoneNumber());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItemRequestDTO dto : req.getItems()) {
            CartItem cartItem = cartItemRepo.findByIdAndCartUserUserName(dto.getCartItemId(), username)
                .orElseThrow(() -> new AccessDeniedException("Cart item not found in your cart"));

            FoodItem foodItem = cartItem.getFoodItem();
            FoodVariant variant = cartItem.getVariant();
            int quantity = cartItem.getQuantity();
            BigDecimal price = variant.getPrice(); // or fallback if variant price is null
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
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
   
        System.out.println(req.getPaymentMethod());
        
        if (req.getPaymentMethod() == PaymentMethod.STRIPE) {
        	order.setPaymentMethod(PaymentMethod.STRIPE);
        	order.setStatus(OrderStatus.PENDING_PAYMENT);
            
        } else if (req.getPaymentMethod() == PaymentMethod.COD) {
        	order.setPaymentMethod(PaymentMethod.COD);
            order.setStatus(OrderStatus.PREPARING); 
           
        }
        Order savedOrder = orderRepo.save(order);
       String stripeUrl = req.getPaymentMethod() == PaymentMethod.STRIPE
    		   ?stripeService.makePayment(savedOrder):null;
      
        return new PlaceOrderResponse(savedOrder.getId(), totalAmount , stripeUrl);
        
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
    
    
    public AdminOrderMetricsDTO getCompleteReport() {
    	AdminOrderMetrics raw = orderRepo.fetchAllOrderMetrics();

        AdminOrderMetricsDTO dto = new AdminOrderMetricsDTO();
        dto.setTotalOrders(raw.getTotalOrders());
        dto.setOrdersToday(raw.getOrdersToday());
        dto.setOrdersYesterday(raw.getOrdersYesterday());
        dto.setOrdersThisWeek(raw.getOrdersThisWeek());
        dto.setOrdersThisMonth(raw.getOrdersThisMonth());

        dto.setRevenueToday(raw.getRevenueToday());
        dto.setRevenueYesterday(raw.getRevenueYesterday());
        dto.setRevenueThisWeek(raw.getRevenueThisWeek());
        dto.setRevenueThisMonth(raw.getRevenueThisMonth());

        dto.setStripePaymentsToday(raw.getStripePaymentsToday());
        dto.setCodPaymentsToday(raw.getCodPaymentsToday());
        dto.setStripePaymentsTotal(raw.getStripePaymentsTotal());
        dto.setCodPaymentsTotal(raw.getCodPaymentsTotal());

        return dto;
    }
    
    
    
    public String retryCheckout(Long orderId) {
        Order order = orderRepo.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order is already paid or canceled");
        }

        return stripeService.makePayment(order);
    }

    public void cancelOrder(Long id, Authentication authentication) {
    	//first Check if order exists and belong to current user
    	//check the time of order , if less than 2 minutes simply cancel order
    	//if more than 2 minutes, decrease the trust score of User
    	
    	 Order order = orderRepo.findByIdAndUserUserName(id,authentication.getName())
    			 .orElseThrow(()->new ResourceNotFoundException("Cannot find order with given id "+id));
    	 CustomUserDetails customUser = (CustomUserDetails )authentication.getPrincipal();
    	 User user = customUser.getUser();
    	 Duration duration = Duration.between(order.getCreatedAt(), LocalDateTime.now());
    	 if(duration.toMinutes()>2) {
    		 user.setTrustScore(user.getTrustScore()-1);    		 
    		 userRepo.save(user);
    	 }
    	 
    	 order.setStatus(OrderStatus.CANCELLED);
    	 orderRepo.save(order);
    	 
    	 
    	 
    }
    
    
    
}

