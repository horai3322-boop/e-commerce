package com.lawlayui.e_commerce.product_inventory.application.port.in;

public record GetInventoryItemQuery(String sku) {
    public GetInventoryItemQuery {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU must not be null or blank");
        }
    }
}


