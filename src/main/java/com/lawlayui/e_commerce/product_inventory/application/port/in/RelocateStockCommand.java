package com.lawlayui.e_commerce.product_inventory.application.port.in;

public record RelocateStockCommand(String sku, String newLocationCode) {
    public RelocateStockCommand {
        if (newLocationCode == null || newLocationCode.isEmpty()) {
            throw new IllegalArgumentException("New LocationCode cannot be null or empty");
        }
    }
}
