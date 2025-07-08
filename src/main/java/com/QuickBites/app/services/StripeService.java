package com.QuickBites.app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.QuickBites.app.entities.Order;
import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

	@Value("${stripe.secret.key}")
	private String stripeSecretKey;
	
	@Value("${stripe.success.Url}")
	private String stripeSuccessUrl;
	
	@Value("${stripe.failure.Url}")
	private String stripeFailureUrl;
	
	@PostConstruct
	public void init() {
		 Stripe.apiKey = stripeSecretKey;
	}
	
	
	public void makePayment(Order order) {
		Long orderId = order.getId();
		//convert the currency to smallestUnit
		BigDecimal totalAmount = order.getTotalAmount();
		Long totalAmountInCents = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();
		
		SessionCreateParams.Builder builder = SessionCreateParams.builder()
						.setMode(SessionCreateParams.Mode.PAYMENT)
						.setSuccessUrl(stripeSuccessUrl+orderId)
						.setCancelUrl(stripeFailureUrl+orderId);
		
		
		
	}
	
}
