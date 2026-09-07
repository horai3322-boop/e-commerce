package com.lawlayui.e_commerce.product_inventory.application.port.in;

public record CreateInventoryCommand(String sku, int quantity, String locationCode) {
    public CreateInventoryCommand {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (locationCode == null || locationCode.isEmpty()) {
            throw new IllegalArgumentException("Location code cannot be null or empty");
        }
    }
}
