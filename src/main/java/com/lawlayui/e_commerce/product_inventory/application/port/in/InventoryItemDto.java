package com.lawlayui.e_commerce.product_inventory.application.port.in;

public record InventoryItemDto(String sku, int quantity, String locationCode) {
    
}
