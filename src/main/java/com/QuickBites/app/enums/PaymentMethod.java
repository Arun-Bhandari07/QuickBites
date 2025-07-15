package com.QuickBites.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentMethod {

	STRIPE,
	COD;
	
	@JsonCreator
    public static PaymentMethod from(String value) {
        if (value == null) throw new IllegalArgumentException("PaymentMethod is null");
        return PaymentMethod.valueOf(value.trim().toUpperCase());
    }
}
