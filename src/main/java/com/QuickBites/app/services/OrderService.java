package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

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
    private final CartItemRepository cartItemRepo;
    private final AddressRepository addressRepo;

    
    public OrderService(OrderRepository orderRepo,
                        UserRepository userRepo,
                        FoodItemRepository foodItemRepo,
                        FoodVariantRepository foodVariantRepo,
                        StripeService stripeService,
                        CartItemRepository cartItemRepo
                        ,AddressRepository addressRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.foodItemRepo = foodItemRepo;
        this.foodVariantRepo = foodVariantRepo;
        this.stripeService = stripeService;
        this.cartItemRepo=cartItemRepo;
        this.addressRepo=addressRepo;
    }

    public PlaceOrderResponse placeOrder(PlaceOrderRequestDTO req, String username){
    	LocationInfo location;
    	
    	
        User user = userRepo.findByUserName(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
        Order savedOrder = orderRepo.save(order);
        order.setPaymentMethod(req.getPaymentMethod());
        
        if (req.getPaymentMethod() == PaymentMethod.STRIPE) {
            String stripeUrl = stripeService.makePayment(savedOrder);
            return new PlaceOrderResponse(savedOrder.getId(), totalAmount, stripeUrl);
        } else if (req.getPaymentMethod() == PaymentMethod.COD) {
            savedOrder.setStatus(OrderStatus.EN_ROUTE); 
            orderRepo.save(savedOrder);
            return new PlaceOrderResponse(savedOrder.getId(), totalAmount, null);
        }
        String stripeUrl = stripeService.makePayment(savedOrder);

        return new PlaceOrderResponse(savedOrder.getId(), totalAmount, stripeUrl);
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

