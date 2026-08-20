package com.lawlayui.e_commerce.product_catalog.application.exception;

public class ProductLengthException extends RuntimeException {
    public ProductLengthException(int max, int min, String data) {
        super("the " + data + " must have a minimum length of " + min + " character and a maximum of " + max);
    }
}
