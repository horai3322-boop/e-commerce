package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.util.Objects;

public record GetProductBySkuQuery(String sku) {
    public GetProductBySkuQuery { 
        Objects.requireNonNull(sku, "SKU cannot be null");

        if (sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be blank");
        }
    }
}
