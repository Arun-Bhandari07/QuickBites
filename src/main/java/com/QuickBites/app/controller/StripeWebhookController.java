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

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/stripe")
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

            if (intent != null && intent.getMetadata() != null) {
                String orderIdStr = intent.getMetadata().get("orderId");
                Long orderId = Long.parseLong(orderIdStr);

                orderRepo.findById(orderId).ifPresent(order -> {
                    order.setStatus(OrderStatus.PAID);
                    orderRepo.save(order);
                });
            }
        }

        return ResponseEntity.ok("Webhook handled");
    }
}

