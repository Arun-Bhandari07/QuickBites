package com.QuickBites.app.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.QuickBites.app.enums.DeliveryStatus;
import com.QuickBites.app.enums.KitchenStatus;
import com.QuickBites.app.enums.OrderStatus;
import com.QuickBites.app.enums.PaymentMethod;

public class OrderResponseDTO {

    private Long orderId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDTO> items;
    private PaymentMethod paymentMethod;
    private OrderStatus orderStatus;
    private KitchenStatus kitchenStatus;
    private DeliveryStatus deliveryStatus;
    private Long deliveryTime;
    private String verificationOtp;

  
    public OrderResponseDTO() {
    }

    
    public OrderResponseDTO(Long orderId, BigDecimal totalAmount,
                            LocalDateTime createdAt, List<OrderItemResponseDTO> items
                            ,PaymentMethod paymentMethod,OrderStatus orderStatus
                            ,KitchenStatus kitchenStatus,DeliveryStatus deliveryStatus,
                            Long deliveryTime, String verificationOtp) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
        this.paymentMethod=paymentMethod;
        this.orderStatus=orderStatus;
        this.kitchenStatus=kitchenStatus;
        this.deliveryStatus=deliveryStatus;
        this.deliveryTime=deliveryTime;
        this.verificationOtp=verificationOtp;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponseDTO> items) {
        this.items = items;
    }


	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public OrderStatus getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}


	public KitchenStatus getKitchenStatus() {
		return kitchenStatus;
	}


	public void setKitchenStatus(KitchenStatus kitchenStatus) {
		this.kitchenStatus = kitchenStatus;
	}


	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}


	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}


	public String getVerificationOtp() {
		return verificationOtp;
	}


	public void setVerificationOtp(String verificationOtp) {
		this.verificationOtp = verificationOtp;
	}


	public Long getDeliveryTime() {
		return deliveryTime;
	}


	public void setDeliveryTime(Long deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
    
}
