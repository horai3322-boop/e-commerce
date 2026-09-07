package com.lawlayui.e_commerce.product_catalog.application.port.in;

public record ChangeCatalogStatusCommand(String sku, int stock) {
    public ChangeCatalogStatusCommand {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be null or blank");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }
}
