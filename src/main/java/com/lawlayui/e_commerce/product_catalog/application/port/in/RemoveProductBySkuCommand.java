package com.lawlayui.e_commerce.product_catalog.application.port.in;

import java.util.Objects;

public record RemoveProductBySkuCommand(String sku) {
    public RemoveProductBySkuCommand {
        Objects.requireNonNull(sku, "SKU cannot be null");

        if (sku.isBlank()) {
            throw new IllegalArgumentException("SKU cannot be blank");
        }
    }
}
