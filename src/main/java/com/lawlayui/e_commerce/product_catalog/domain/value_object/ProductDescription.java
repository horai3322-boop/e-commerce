package com.lawlayui.e_commerce.product_catalog.domain.value_object;

import java.util.Objects;

public record ProductDescription(String desc) {
    public ProductDescription {
        Objects.requireNonNull(desc, "ProductDescription cannot be null");
    }
}
