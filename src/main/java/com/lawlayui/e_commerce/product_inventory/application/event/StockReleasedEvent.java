package com.lawlayui.e_commerce.product_inventory.application.event;

public record StockReleasedEvent(String sku, int quantity) {
    
}
