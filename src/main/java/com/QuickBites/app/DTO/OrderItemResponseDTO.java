package com.QuickBites.app.DTO;

import java.math.BigDecimal;

public class OrderItemResponseDTO {

    private Long itemId;
    private String foodName;
    private String variantName; 
    private int quantity;
    private BigDecimal priceAtPurchase;

  
    public OrderItemResponseDTO() {
    }

    public OrderItemResponseDTO(Long itemId, String foodName, String variantName, int quantity, BigDecimal priceAtPurchase) {
        this.itemId = itemId;
        this.foodName = foodName;
        this.variantName = variantName;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }


    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}