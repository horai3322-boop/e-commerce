package com.lawlayui.e_commerce.product_catalog.application.port.in;

public record RemoveProductCommand(
    String productId
) {
    public RemoveProductCommand {
        if (productId == null || productId.isEmpty()) {
            throw new IllegalArgumentException("productId cannot be null or empty");
        }
    }   
}
