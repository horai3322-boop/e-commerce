package com.lawlayui.e_commerce.product_catalog.application.event;

public record ProductCreatedEvent(String productId, int initialStock) {
}
