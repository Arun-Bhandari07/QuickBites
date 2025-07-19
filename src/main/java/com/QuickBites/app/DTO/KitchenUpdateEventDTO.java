package com.QuickBites.app.DTO;

public class KitchenUpdateEventDTO {

	 private Long orderId;
	    private String deliveryStatus;
	    	
	    
	    public KitchenUpdateEventDTO(Long orderId, String deliveryStatus) {
	        this.orderId = orderId;
	        this.deliveryStatus = deliveryStatus;
	  
	    }
		public Long getOrderId() {
			return orderId;
		}
		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}
		public String getDeliveryStatus() {
			return deliveryStatus;
		}
		public void setDeliveryStatus(String deliveryStatus) {
			this.deliveryStatus = deliveryStatus;
		}
	    
	    
	
}
