package com.QuickBites.app.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class FoodItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private BigDecimal price;
	private String description;
	private String imageUrl;
	private boolean isActive;
	
	@ManyToOne
 	private FoodCategory category;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy="foodItem", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<FoodVariant> foodVariants;
	
	public FoodCategory getCategory() {
		return category;
	}

	public void setCategory(FoodCategory category) {
		this.category = category;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<FoodVariant> getFoodVariants() {
		return foodVariants;
	}

	public void setFoodVariants(List<FoodVariant> foodVariants) {
		this.foodVariants = foodVariants;
	}
	
	 
	
}
