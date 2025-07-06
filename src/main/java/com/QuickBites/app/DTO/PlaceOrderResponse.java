package com.QuickBites.app.DTO;

import java.math.BigDecimal;

public class PlaceOrderResponse {

	 	private Long orderId;
	    private BigDecimal totalAmount;
	    private String clientSecret;
	    
	    public PlaceOrderResponse(Long orderId, BigDecimal totalAmount, String clientSecret) {
	    	this.orderId=orderId;
	    	this.totalAmount=totalAmount;
	    	this.clientSecret=clientSecret;
	    }
	    
		public Long getOrderId() {
			return orderId;
		}
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		public BigDecimal getTotalAmount() {
			return totalAmount;
		}
		public void setTotalAmount(BigDecimal totalAmount) {
			this.totalAmount = totalAmount;
		}
		public String getClientSecret() {
			return clientSecret;
		}
		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}
	    
}
