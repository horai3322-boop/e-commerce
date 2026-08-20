package com.lawlayui.e_commerce.product_catalog.application.exception;

public class ProductPriceZeroException extends RuntimeException {
    public ProductPriceZeroException() {
        super("Price cannot be zero");
    }
}
