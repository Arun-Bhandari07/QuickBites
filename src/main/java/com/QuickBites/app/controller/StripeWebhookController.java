package com.QuickBites.app.controller;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.entities.Order;
import com.QuickBites.app.enums.OrderStatus;
import com.QuickBites.app.repositories.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
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
    	String payload;
    	
    	//Extract signature header from request
    	String sigHeader = request.getHeader("Stripe-Signature");
    	
    	//read the payload with scanner try-with resource
    	try(Scanner scanner = new Scanner(request.getInputStream()).useDelimiter("\\A")){
    		payload = scanner.hasNext() ? scanner.next() : "";	
    	}catch(IOException ex) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to read  payload");
    	}
    	
    	Event event;
    	
    	//verify if the incoming request comes from stripe with payload,signature and secretKey
    	try {
    		event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
    	}catch(SignatureVerificationException ex) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Signature");
    	}
    	
    	System.out.println("HELOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    	
    	//check the status of payment and update order status
    	if("checkout.session.completed".equals(event.getType())) {
    		Session session = (Session) event.getDataObjectDeserializer()
    					.getObject()
    					.orElseThrow(()->new RuntimeException("Failed to deserialize session object"));
    		
    		String orderIdStr = session.getMetadata().get("orderId");
    		if(orderIdStr!=null) {
    			Long orderId = Long.valueOf(orderIdStr);
    			Order order = orderRepo.findById(orderId)
    						.orElse(null);
    				if(order!=null && order.getStatus()==OrderStatus.PREPARING) {
    					order.setStatus(OrderStatus.PAID);
    					orderRepo.save(order);
    					System.out.println("Order #"+orderId+" marked as PAID");
    				}
    		}
    	}
    	return ResponseEntity.ok("Webhook Received");
    }
       
}

