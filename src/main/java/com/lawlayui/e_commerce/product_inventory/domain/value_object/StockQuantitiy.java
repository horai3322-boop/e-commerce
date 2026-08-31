package com.lawlayui.e_commerce.product_inventory.domain.value_object;

public record StockQuantitiy(Integer value) {
    public StockQuantitiy {
        if (value == null) {
            throw new IllegalArgumentException("StockQuantity cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("StockQuantity cannot be negative");
        }
    }
}
