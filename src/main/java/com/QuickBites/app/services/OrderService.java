package com.QuickBites.app.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
import com.QuickBites.app.repositories.OrderItemRepository;
import com.QuickBites.app.repositories.OrderRepository;
import com.QuickBites.app.repositories.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final UserRepository userRepo;
    private final FoodItemRepository foodItemRepo;
    private final FoodVariantRepository foodVariantRepo;

    
    public OrderService(OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo,
                        UserRepository userRepo,
                        FoodItemRepository foodItemRepo,
                        FoodVariantRepository foodVariantRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.userRepo = userRepo;
        this.foodItemRepo = foodItemRepo;
        this.foodVariantRepo = foodVariantRepo;
    }

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    public PlaceOrderResponse placeOrder(PlaceOrderRequestDTO request, String username) {
        // 1. Fetch User
        User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 2. Calculate Total and Prepare OrderItems
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItemRequestDTO dto : request.getItems()) {
            FoodItem food = foodItemRepo.findById(dto.getFoodItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food item not found"));

            FoodVariant variant = null;
            if (dto.getVariantId() != null) {
                variant = foodVariantRepo.findById(dto.getVariantId())
                        .orElseThrow(() -> new ResourceNotFoundException("Variant not found"));
            }

            BigDecimal unitPrice = food.getPrice(); // if variant has price override, apply here

            BigDecimal itemTotal = unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setFoodItem(food);
            orderItem.setVariant(variant);
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPriceAtPurchase(unitPrice);

            orderItems.add(orderItem);
        }

        // 3. Create Order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setTotalAmount(totalAmount);
        order.setItems(orderItems); // will be saved cascade

        orderItems.forEach(item -> item.setOrder(order)); // Set reverse reference

        orderRepo.save(order); // saves everything

        // 4. Create Stripe PaymentIntent
        Stripe.apiKey = stripeSecretKey;

        long amountInSmallestUnit = totalAmount.multiply(BigDecimal.valueOf(100)).longValue(); // cents / paisa

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInSmallestUnit)
                .setCurrency("usd") // or "npr" or whatever your business uses
                .putAllMetadata(Map.of("orderId", order.getId().toString()))
                .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            String clientSecret = intent.getClientSecret();

            return new PlaceOrderResponse(order.getId(), totalAmount, clientSecret);
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage());
        }
    }
    
    
    public Order getOrderByIdForUser(Long orderId, String username) {
        return orderRepo.findByIdAndUserUserName(orderId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
    
    
    public List<Order> getOrdersForUser(String username) {
        return orderRepo.findByUserUserName(username);
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
    
}

