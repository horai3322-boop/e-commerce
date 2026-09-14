package com.lawlayui.e_commerce.product_inventory.application.port.in;

public record ReplenishStockCommand(String sku, int quantity) {
    public ReplenishStockCommand {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
    
}
