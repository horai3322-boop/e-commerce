package com.lawlayui.e_commerce.product_inventory.application.event;

public record StockDeductedEvent(String sku, int quantity) {
} 