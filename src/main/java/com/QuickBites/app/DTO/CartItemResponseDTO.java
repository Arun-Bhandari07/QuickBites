package com.QuickBites.app.DTO;

import java.math.BigDecimal;

public class CartItemResponseDTO {

	private Long id;
    private String foodName;
    private String variantName;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal subtotal;
    
    public CartItemResponseDTO(Long id, String foodName, String variantName,
            BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
    	this.id = id;
    	this.foodName = foodName;
    	this.variantName = variantName;
    	this.unitPrice = unitPrice;
    	this.quantity = quantity;
    	this.subtotal = subtotal;
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public String getVariantName() {
		return variantName;
	}
	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
    
    
}
