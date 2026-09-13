package com.lawlayui.e_commerce.product_catalog.application.exception;

public class CatalogNotFoundException extends RuntimeException {
    public CatalogNotFoundException(String sku) {
        super("Product with SKU " + sku + " not found");
    }
}
