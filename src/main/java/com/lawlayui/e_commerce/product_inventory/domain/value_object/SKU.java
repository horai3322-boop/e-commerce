package com.lawlayui.e_commerce.product_inventory.domain.value_object;

public record SKU(String value) {
    public SKU {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or blank");
        }
    }
    
}
