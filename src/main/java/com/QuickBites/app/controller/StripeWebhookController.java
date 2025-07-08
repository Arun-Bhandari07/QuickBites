package com.QuickBites.app.controller;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.enums.OrderStatus;
import com.QuickBites.app.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/stripe")
@Tag(name="Stripe WebHook API")
public class StripeWebhookController {

    private  OrderRepository orderRepo;

    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;
    
    public StripeWebhookController(OrderRepository orderRepo) {
    	this.orderRepo = orderRepo;
    }

    
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) throws IOException {
        String payload = request.getReader().lines().collect(Collectors.joining());
        String sigHeader = request.getHeader("Stripe-Signature");
        
        System.out.println("🎯 Stripe webhook hit: "+payload );
        
        Event event;
        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    stripeWebhookSecret
            );
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Only care about payment success
        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);
            	
            if (intent == null) {
                System.out.println("Could not deserialize PaymentIntent");
                return ResponseEntity.ok("Ignored");
            }
            
            
            if (intent != null && intent.getMetadata() != null) {
                String orderIdStr = intent.getMetadata().get("orderId");
                
                if (orderIdStr != null) {
                    Long orderId = Long.parseLong(orderIdStr);
                    
                    orderRepo.findById(orderId).ifPresent(order -> {
                        order.setStatus(OrderStatus.PAID);
                        orderRepo.save(order);
                    });
                } else {
                    System.out.println(" Missing orderId in metadata");
                }
            }
        }
        System.out.println(" Webhook processed successfully");
        return ResponseEntity.ok("Webhook handled");
    }
}

