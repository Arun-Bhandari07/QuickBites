package com.QuickBites.app.entities;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.QuickBites.app.enums.DeliveryStatus;
import com.QuickBites.app.enums.KitchenStatus;
import com.QuickBites.app.enums.OrderStatus;
import com.QuickBites.app.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders") 
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    private BigDecimal totalAmount;

    @Column(name="orderStatus")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; 

    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @Column(nullable =true)
    private BigDecimal deliveryCharge  = BigDecimal.ZERO;
    
    @Embedded
	LocationInfo locationInfo;
    
    private String phone;
    
    private String specialInstructions;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    private KitchenStatus kitchenStatus;
    
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    
    @ManyToOne
    @JoinColumn(name = "delivery_agent_id")
    private DeliveryAgent assignedAgent;
    
    private String verificationOtp;
    
    private Long deliveryTime;
    

	public LocationInfo getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(LocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus status) {
		this.orderStatus = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSpecialInstructions() {
		return specialInstructions;
	}

	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	public DeliveryAgent getAssignedAgent() {
		return assignedAgent;
	}

	public void setAssignedAgent(DeliveryAgent assignedAgent) {
		this.assignedAgent = assignedAgent;
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

	public BigDecimal getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(BigDecimal deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
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
