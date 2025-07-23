package com.QuickBites.app.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.QuickBites.app.enums.DeliveryStatus;
import com.QuickBites.app.enums.PaymentMethod;

public class DeliveryHistoryDTO {
	private Long orderId;
	private String customerName;
	private String deliveryAddress;
	private LocalDateTime deliveryDateTime;
	private BigDecimal totalAmount;
	private PaymentMethod paymentMethod;
	private DeliveryStatus finalDeliveryStatus;
	private List<OrderItemResponseDTO> orderItems;

	public DeliveryHistoryDTO() {
	}

	public DeliveryHistoryDTO(Long orderId, String customerName, String deliveryAddress,
			LocalDateTime deliveryDateTime, BigDecimal totalAmount, PaymentMethod paymentMethod,
			DeliveryStatus finalDeliveryStatus, List<OrderItemResponseDTO> orderItems) {
		this.orderId = orderId;
		this.customerName = customerName;
		this.deliveryAddress = deliveryAddress;
		this.deliveryDateTime = deliveryDateTime;
		this.totalAmount = totalAmount;
		this.paymentMethod = paymentMethod;
		this.finalDeliveryStatus = finalDeliveryStatus;
		this.orderItems = orderItems;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public LocalDateTime getDeliveryDateTime() {
		return deliveryDateTime;
	}

	public void setDeliveryDateTime(LocalDateTime deliveryDateTime) {
		this.deliveryDateTime = deliveryDateTime;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public DeliveryStatus getFinalDeliveryStatus() {
		return finalDeliveryStatus;
	}

	public void setFinalDeliveryStatus(DeliveryStatus finalDeliveryStatus) {
		this.finalDeliveryStatus = finalDeliveryStatus;
	}

	public List<OrderItemResponseDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
		this.orderItems = orderItems;
	}

}
