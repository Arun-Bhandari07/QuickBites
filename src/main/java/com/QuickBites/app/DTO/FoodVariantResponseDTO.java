package com.QuickBites.app.DTO;

import java.math.BigDecimal;

import com.QuickBites.app.entities.FoodItem;

public class FoodVariantResponseDTO {
	private Long id;

	private String name;
	
	private BigDecimal price;
	
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
