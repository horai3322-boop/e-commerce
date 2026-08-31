package com.lawlayui.e_commerce.product_inventory.domain.value_object;

public record ProductId(String value) {
    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ProductId cannot be null or blank");
        }
    }

    public static ProductId generateId() {
        return new ProductId(java.util.UUID.randomUUID().toString());
    }
}
