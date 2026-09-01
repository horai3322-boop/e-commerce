package com.lawlayui.e_commerce.product_inventory.application.port.in;

public record DeductReservedCommand(String sku, int quantity) {
    public DeductReservedCommand {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }

}
