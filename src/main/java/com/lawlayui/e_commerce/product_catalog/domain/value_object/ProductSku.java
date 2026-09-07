package com.lawlayui.e_commerce.product_catalog.domain.value_object;

public record ProductSku(String value) {
    public ProductSku {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Product SKU cannot be null or empty");
        }
    } 
}
