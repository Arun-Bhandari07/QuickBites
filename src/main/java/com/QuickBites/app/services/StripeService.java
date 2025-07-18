package com.QuickBites.app.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.QuickBites.app.entities.Order;
import com.QuickBites.app.entities.OrderItem;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
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
	
	public String makePayment(Order order) {
		Long orderId = order.getId();
		
		SessionCreateParams.Builder builder = SessionCreateParams.builder()
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(stripeSuccessUrl)
				.setCancelUrl(stripeFailureUrl);
		
		
		for(OrderItem item:order.getItems()) {
			Long totalAmountInPaisa = item.getPriceAtPurchase()
					.multiply(BigDecimal.valueOf(100)).longValue();
			
			SessionCreateParams.LineItem lineItem =SessionCreateParams.LineItem.builder()
						.setQuantity((long)item.getQuantity())
						.setPriceData(
								SessionCreateParams.LineItem.PriceData.builder()
										.setCurrency("npr")
										.setUnitAmount(totalAmountInPaisa)
										.setProductData(
												SessionCreateParams.LineItem.PriceData.ProductData.builder()
													.setName(item.getFoodItem().getName())
													.build()
														)
											.build()
										)
						.build();
						
			builder.addLineItem(lineItem);
		}
		long deliveryChargeInPaisa = order.getDeliveryCharge()
				.multiply(BigDecimal.valueOf(100)).longValue();
		
		SessionCreateParams.LineItem deliveryChargeItem = SessionCreateParams.LineItem.builder()
					.setQuantity(1L)
					.setPriceData(
							SessionCreateParams.LineItem.PriceData.builder()
							.setCurrency("npr")
							.setUnitAmount(deliveryChargeInPaisa)
							.setProductData(
									SessionCreateParams.LineItem.PriceData.ProductData.builder()
									   .setName("Delivery Charge")
									   .build()
									).build()
							).build();
	
		builder.addLineItem(deliveryChargeItem);
		builder.putMetadata("orderId", order.getId().toString());
		
		SessionCreateParams params = builder.build();
		try {
			Session session = Session.create(params);
			return session.getUrl();
		}catch(StripeException ex) {
			System.err.println("Error creating Stripe Checkout Session for order " + orderId + ": " + ex.getMessage());
            ex.printStackTrace();
            return null;
		}
		
	}
	
}
