package com.QuickBites.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.OrderResponseDTO;
import com.QuickBites.app.DTO.PlaceOrderRequestDTO;
import com.QuickBites.app.DTO.PlaceOrderResponse;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name="Order" ,description="Place and Get Orders")
public class OrderController {

    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
    	this.orderService=orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<PlaceOrderResponse> placeOrder(
            @RequestBody PlaceOrderRequestDTO request,
            Authentication authentication) {
        String username = authentication.getName();
        PlaceOrderResponse response = orderService.placeOrder(request, username);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") Long id, Authentication auth) {
        String username = auth.getName();
        Order order = orderService.getOrderByIdForUser(id, username);
        OrderResponseDTO dto = orderService.convertToResponseDTO(order);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrdersForUser(Authentication authentication) {
        String username = authentication.getName();
        List<Order> orders = orderService.getOrdersForUser(username);
        return ResponseEntity.ok(orders);
    }
}
