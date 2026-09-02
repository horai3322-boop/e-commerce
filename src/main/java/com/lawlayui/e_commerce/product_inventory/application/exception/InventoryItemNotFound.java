package com.lawlayui.e_commerce.product_inventory.application.exception;

public class InventoryItemNotFound extends RuntimeException {
    public InventoryItemNotFound(String sku) {
        super("Inventory item not found for SKU: " + sku);
    }
    
}
