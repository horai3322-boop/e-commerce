package com.lawlayui.e_commerce.product_catalog.application.event;

public record ProductPhotoChangedEvent(String productId, String oldPhoto, String newPhoto) {
    
}
