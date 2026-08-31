package com.lawlayui.e_commerce.product_inventory.domain.value_object;

import java.util.UUID;

public record InventoryItemId(String value) {
    public InventoryItemId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("InventoryItemId cannot be null or blank");
        }
    }

    public static InventoryItemId generateId() {
        return new InventoryItemId(UUID.randomUUID().toString());
    }
    
}
