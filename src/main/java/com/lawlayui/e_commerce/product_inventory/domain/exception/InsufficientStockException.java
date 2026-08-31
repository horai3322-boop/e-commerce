package com.lawlayui.e_commerce.product_inventory.domain.exception;

public class InsufficientStockException extends RuntimeException{
    public InsufficientStockException() {
        super("Insufficient stock to reserve");
    }
}
