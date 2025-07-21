package com.QuickBites.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.DTO.OrderResponseDTO;
import com.QuickBites.app.DTO.PlaceOrderRequestDTO;
import com.QuickBites.app.DTO.PlaceOrderResponse;
import com.QuickBites.app.entities.Order;
import com.QuickBites.app.services.DeliveryChargeService;
import com.QuickBites.app.services.DeliveryChargeService.DeliveryInfo;
import com.QuickBites.app.services.OrderService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name="Order" ,description="Place and Get Orders")
public class OrderController {

    private final OrderService orderService;
    private final DeliveryChargeService deliveryChargeService;
    
    private record deliveryLocation(double lat , double lon) {};
    
    
    public OrderController(OrderService orderService,DeliveryChargeService deliveryChargeService) {
    	this.orderService=orderService;
    	this.deliveryChargeService=deliveryChargeService;
    }
    
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrdersForUser(Authentication authentication) {
    	String username = authentication.getName();
    	List<OrderResponseDTO> dtoList = orderService.getOrdersForUser(username);
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/place")
    public ResponseEntity<PlaceOrderResponse> placeOrder(@Valid
            @RequestBody PlaceOrderRequestDTO request,
            Authentication authentication) {
        String username = authentication.getName();
        PlaceOrderResponse response = orderService.placeOrder(request, username);
        return ResponseEntity.ok(response);
    }
    
    
   @PutMapping("/cancel/{id}")
   public void cancelOrder(@PathVariable(name="id") Long id, Authentication auth) {
	   	orderService.cancelOrder(id , auth);
   }
   

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") Long id, Authentication auth) {
        String username = auth.getName();
        Order order = orderService.getOrderByIdForUser(id, username);
        OrderResponseDTO dto = orderService.convertToResponseDTO(order);
        return ResponseEntity.ok(dto);
    }

   
      
    @PostMapping("/{orderId}/retry-checkout")
    public ResponseEntity<?> retryPayment(@PathVariable Long orderId) {
        try {
            String stripeUrl = orderService.retryCheckout(orderId);
            return ResponseEntity.ok(Map.of("stripeUrl", stripeUrl));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }
    
    @PostMapping("/deliveryCharge")
    public ResponseEntity<?> calculateDeliveryCharge(@RequestBody deliveryLocation location){
    	DeliveryInfo info = deliveryChargeService.calculateDeliveryChargeAndTime(location.lat(), location.lon());
    	System.out.println("Calculated Delivery time: "+info.deliveryTime()+" minutes "+"Calculate deliveryCharge: "+info.deliveryCharge());
    	return ResponseEntity.ok().body(info.deliveryCharge());
    }
    
}
