package com.lawlayui.e_commerce.product_inventory.application.event;

public record StockRelocatedEvent(String sku, String fromLocation, String toLocation, int quantity) {
    
}
