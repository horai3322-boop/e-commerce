package com.lawlayui.e_commerce.product_inventory.application.event;

public record InventoryCreatedEvent(String id, String sku, int quantity, String locationCode) {
    
}
