package com.QuickBites.app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FoodVariant {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private BigDecimal price;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="food_item_id")
	private FoodItem foodItem;
	
	public FoodItem getFoodItem() {
		return foodItem;
	}

	public void setFoodItem(FoodItem foodItem) {
		this.foodItem = foodItem;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	


	

}
