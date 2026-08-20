package com.lawlayui.e_commerce.product_catalog.application.exception;

public class ProductDataEmptyException extends RuntimeException {
    public ProductDataEmptyException(String data) {
        super(data + " cannot be empty");
    }
}
