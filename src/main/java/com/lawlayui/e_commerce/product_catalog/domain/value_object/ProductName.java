package com.lawlayui.e_commerce.product_catalog.domain.value_object;

import java.util.Objects;

public record ProductName(String productName) {
    public ProductName {
        Objects.requireNonNull(productName, "ProductName cannot be null");
    }
}
