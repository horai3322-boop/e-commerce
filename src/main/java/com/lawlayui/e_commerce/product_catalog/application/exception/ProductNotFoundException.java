package com.lawlayui.e_commerce.product_catalog.application.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String productId) {
        super("Product with ID " + productId + " not found.");
    }
    
}
