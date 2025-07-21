package com.QuickBites.app.DTO;

public class AgentNotificationDTO {

	private Long orderId;
	
	public AgentNotificationDTO(Long orderId) {
	this.orderId=orderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	
}
